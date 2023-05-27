package mju.dalmutiserver.service;

import mju.dalmutiserver.dto.GetRoomDto;
import mju.dalmutiserver.entity.Room;
import mju.dalmutiserver.entity.User;
import mju.dalmutiserver.entity.UserAndRoom;
import mju.dalmutiserver.exception.CannotAccessRoomException;
import mju.dalmutiserver.exception.InputException;
import mju.dalmutiserver.exception.RoomNotFoundException;
import mju.dalmutiserver.repository.RoomRepository;
import mju.dalmutiserver.repository.UserAndRoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
   private final RoomRepository roomRepository;
   private final UserAndRoomRepository userAndRoomRepository;

    public RoomService(RoomRepository roomRepository, UserAndRoomRepository userAndRoomRepository) {
        this.roomRepository = roomRepository;
        this.userAndRoomRepository = userAndRoomRepository;
    }

    public Room newRoom(User user, String name, String mode) {
        validateRoomInput(name, mode);

        Room newRoom = Room.builder()
                        .name(name)
                        .mode(mode)
                        .userAndRooms(new ArrayList<>())
                        .build();
        UserAndRoom userAndRoom = UserAndRoom.builder()
                        .room(newRoom)
                        .user(user)
                        .build();
        newRoom.getUserAndRooms().add(userAndRoom);
        roomRepository.save(newRoom);
        userAndRoomRepository.save(userAndRoom);
        return newRoom;
    }

    private static void validateRoomInput(String name, String mode) {
        if (name == null || mode == null || (!mode.equals("multi") && !mode.equals("single"))) {
            throw new InputException("입력이 잘못되었습니다.");
        }
    }

    public GetRoomDto findRoom(Long room_id) {
        Room targetRoom = roomRepository.findById(room_id).orElseThrow(
                () -> new RoomNotFoundException("방이 없습니다.")
        );

        List<UserAndRoom> userAndRooms = userAndRoomRepository.findByRoomId(room_id);

        List<User> users = userAndRooms.stream()
                .map(UserAndRoom::getUser)
                .collect(Collectors.toList());

        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .mode(targetRoom.getMode())
                .users(users)
                .build();
    }

    // 사람 간 플레이하는 방에는 4명까지 들어갈 수 있어야 하며, AI 전용이라면 작성자만 들어올 수 있음
    public GetRoomDto join(Long room_id, User user) {
        Room targetRoom = roomRepository.findById(room_id).orElseThrow(
                () -> new RoomNotFoundException("방이 없습니다.")
        );

        User owner = checkRoomAndUserAuthority(room_id, user, targetRoom);

        List<UserAndRoom> userAndRooms = userAndRoomRepository.findByRoomId(room_id);
        List<User> users = userAndRooms.stream()
                .map(UserAndRoom::getUser)
                .collect(Collectors.toList());

        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .mode(targetRoom.getMode())
                .users(users)
                .build();
    }

    private User checkRoomAndUserAuthority(Long room_id, User user, Room room) {
        UserAndRoom existUserAndRoom = userAndRoomRepository.findByRoomIdAndUserId(room_id, user.getId())
                .orElse(null);

        if (room.getMode().equals("single")) {
            if (existUserAndRoom == null || !existUserAndRoom.getUser().equals(user)) {
                throw new CannotAccessRoomException("입장할 수 없습니다.");
            }
        } else if (room.getMode().equals("multi")) {
            int roomCapacity = 4; // 방의 최대 수용 인원
            List<UserAndRoom> roomUsers = userAndRoomRepository.findByRoomId(room_id);

            if (roomUsers.size() >= roomCapacity) {
                throw new CannotAccessRoomException("더 이상 입장할 수 없습니다.");
            }

            if (existUserAndRoom == null) {
                // 새로운 유저가 입장하는 경우 UserAndRoom 엔티티 생성
                UserAndRoom newUserAndRoom = UserAndRoom.builder()
                        .room(room)
                        .user(user)
                        .build();

                userAndRoomRepository.save(newUserAndRoom);
            }
        }

        return user;
    }
}
