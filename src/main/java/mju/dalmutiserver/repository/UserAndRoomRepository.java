package mju.dalmutiserver.repository;

import mju.dalmutiserver.entity.UserAndRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {
    List<UserAndRoom> findAllByRoomId(Long roomId);
    UserAndRoom findByRoomIdAndUserId(Long roomId, Long userId);
}
