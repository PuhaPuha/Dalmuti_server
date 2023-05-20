package mju.dalmutiserver.service;

import mju.dalmutiserver.dto.GetRoomDto;
import mju.dalmutiserver.entity.Room;
import mju.dalmutiserver.entity.User;
import mju.dalmutiserver.entity.UserAndRoom;
import mju.dalmutiserver.exception.CannotAccessRoomException;
import mju.dalmutiserver.exception.RoomNotFoundException;
import mju.dalmutiserver.repository.RoomRepository;
import mju.dalmutiserver.repository.UserAndRoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RoomService {
   private final RoomRepository roomRepository;
   private final UserAndRoomRepository userAndRoomRepository;

    public RoomService(RoomRepository roomRepository, UserAndRoomRepository userAndRoomRepository) {
        this.roomRepository = roomRepository;
        this.userAndRoomRepository = userAndRoomRepository;
    }

    public Room newRoom(User user, String name) {
        Room newRoom = Room.builder()
                        .name(name)
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

    public GetRoomDto findRoom(Long room_id) {
        Room targetRoom = roomRepository.findById(room_id).orElseThrow(
                () -> new RoomNotFoundException("방이 없습니다.")
        );

        User owner = userAndRoomRepository.findByRoomId(room_id).getUser();
        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .user(owner)
                .build();
    }

    public GetRoomDto join(Long room_id, User user) {
        Room targetRoom = roomRepository.findById(room_id).orElseThrow(
                () -> new RoomNotFoundException("방이 없습니다.")
        );

        User owner = getOwner(room_id, user);
        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .user(owner)
                .build();
    }

    private User getOwner(Long room_id, User user) {
        UserAndRoom existUserAndRoom = userAndRoomRepository.findByRoomIdAndUserId(room_id, user.getId())
                .orElseThrow(() -> new CannotAccessRoomException("입장할 수 없습니다."));
        return existUserAndRoom.getUser();
    }
}
