package mju.dalmutiserver.repository;

import mju.dalmutiserver.entity.UserAndRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {
    UserAndRoom findByRoomId(Long roomId);
    Optional<UserAndRoom> findByRoomIdAndUserId(Long roomId, Long userId);
}
