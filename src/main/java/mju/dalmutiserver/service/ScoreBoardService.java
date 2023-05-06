package mju.dalmutiserver.service;

import mju.dalmutiserver.entity.Room;
import mju.dalmutiserver.entity.User;
import mju.dalmutiserver.entity.UserAndRoom;
import mju.dalmutiserver.repository.RoomRepository;
import mju.dalmutiserver.repository.UserAndRoomRepository;
import mju.dalmutiserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScoreBoardService {

    private final UserAndRoomRepository userAndRoomRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ScoreBoardService(UserAndRoomRepository userAndRoomRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.userAndRoomRepository = userAndRoomRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserAndRoom gameFinish(Long roomId, Long gameScore, User user) {
        Room targetRoom = roomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("방 없음")
        );
        User targetUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new RuntimeException("유저 없음")
        );
        UserAndRoom targetUserAndRoom = userAndRoomRepository.findByRoomIdAndUserId(
                        targetRoom.getId(), targetUser.getId());

        targetUserAndRoom.setGrade(gameScore);
        targetUserAndRoom.setScore(calculateScore(gameScore));

        return targetUserAndRoom;
    }

    // 특정 기록 조회
    public UserAndRoom getOneScoreInfo(Long roomId, User user) {
        Room targetRoom = roomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("방 없음")
        );
        User targetUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new RuntimeException("유저 없음")
        );

        return userAndRoomRepository.findByRoomIdAndUserId(
                targetRoom.getId(), targetUser.getId());
    }

    // 전체 기록 조회 (자신 제외 포함)
    public List<UserAndRoom> getAllScoreInfo() {
        return userAndRoomRepository.findAll();
    }

    private Long calculateScore(Long score) {
        if (score >= 4) {
            return 10L;
        }
        return 40 - (score - 1) * 10L;
    }
}
