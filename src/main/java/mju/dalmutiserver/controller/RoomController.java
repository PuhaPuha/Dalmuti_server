package mju.dalmutiserver.controller;

import mju.dalmutiserver.dto.RoomDto;
import mju.dalmutiserver.exception.ExceptionResponse;
import mju.dalmutiserver.service.RoomService;
import mju.dalmutiserver.service.ScoreBoardService;
import mju.dalmutiserver.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;
    private final ScoreBoardService scoreBoardService;
    private final UserUtil userUtil;

    public RoomController(RoomService roomService, ScoreBoardService scoreBoardService, UserUtil userUtil) {
        this.roomService = roomService;
        this.scoreBoardService = scoreBoardService;
        this.userUtil = userUtil;
    }

    // 룸 생성
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDto roomDto) {
        try {
            return ResponseEntity.ok().body(roomService.newRoom(userUtil.getLoggedInUser(), roomDto.getName()));
        } catch (Exception e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
    // 룸 조회
    @GetMapping("/{room_id}")
    public ResponseEntity<?> getRoom(@PathVariable Long room_id) {
        try {
            return ResponseEntity.ok(roomService.findRoom(room_id));
        } catch (Exception e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
    // 룸 입장 (게임 시작)
    @PostMapping("/{room_id}")
    public ResponseEntity<?> joinRoom(@PathVariable Long room_id) {
        try {
            return ResponseEntity.ok(roomService.join(room_id, userUtil.getLoggedInUser()));
        } catch (Exception e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    // 게임 종료
    @PostMapping("/{room_id}/end")
    public ResponseEntity<?> gameEnd(@PathVariable Long room_id, @RequestParam(defaultValue = "0") Long score) {
        try {
            return ResponseEntity.ok(scoreBoardService.gameFinish(room_id, score, userUtil.getLoggedInUser()));
        } catch (Exception e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
