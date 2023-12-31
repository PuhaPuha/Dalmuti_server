package mju.dalmutiserver.repository;

import mju.dalmutiserver.entity.UserAndRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {
    List<UserAndRoom> findByRoomId(Long roomId);
    Optional<UserAndRoom> findByRoomIdAndUserId(Long roomId, Long userId);
}
