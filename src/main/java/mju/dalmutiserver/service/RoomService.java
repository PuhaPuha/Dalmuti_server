package mju.dalmutiserver.service;

import mju.dalmutiserver.dto.GetRoomDto;
import mju.dalmutiserver.entity.Room;
import mju.dalmutiserver.entity.User;
import mju.dalmutiserver.entity.UserAndRoom;
import mju.dalmutiserver.repository.RoomRepository;
import mju.dalmutiserver.repository.UserAndRoomRepository;
import mju.dalmutiserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RoomService {
   private final RoomRepository roomRepository;
   private final UserAndRoomRepository userAndRoomRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository, UserAndRoomRepository userAndRoomRepository,
                       UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userAndRoomRepository = userAndRoomRepository;
        this.userRepository = userRepository;
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
                RuntimeException::new
        );
        ArrayList<UserAndRoom> userAndRooms = (ArrayList<UserAndRoom>) userAndRoomRepository.findAllByRoomId(room_id);
        ArrayList<User> users = new ArrayList<>();
        for (UserAndRoom userAndRoom : userAndRooms) {
            users.add(userAndRoom.getUser());
        }
        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .users(users)
                .build();
    }

    public GetRoomDto join(Long room_id, User user) {
        Room targetRoom = roomRepository.findById(room_id).orElseThrow(
                RuntimeException::new
        );
        UserAndRoom existUserAndRoom = userAndRoomRepository.findByRoomIdAndUserId(room_id, user.getId());
        if (existUserAndRoom == null) {
            UserAndRoom newUserAndRoom = UserAndRoom.builder()
                                                    .user(user)
                                                    .room(targetRoom)
                                                    .build();
            userAndRoomRepository.save(newUserAndRoom);
        }
        ArrayList<UserAndRoom> userAndRooms = (ArrayList<UserAndRoom>) userAndRoomRepository.findAllByRoomId(room_id);
        ArrayList<User> users = new ArrayList<>();
        for (UserAndRoom userAndRoom : userAndRooms) {
            users.add(userAndRoom.getUser());
        }
        return GetRoomDto.builder()
                .id(targetRoom.getId())
                .users(users)
                .build();
    }
}
