package mju.dalmutiserver.controller;

import mju.dalmutiserver.entity.UserAndRoom;
import mju.dalmutiserver.exception.ExceptionResponse;
import mju.dalmutiserver.service.ScoreBoardService;
import mju.dalmutiserver.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/score")
public class ScoreBoardController {

    private final ScoreBoardService scoreBoardService;
    private final UserUtil userUtil;

    public ScoreBoardController(ScoreBoardService scoreBoardService, UserUtil userUtil) {
        this.scoreBoardService = scoreBoardService;
        this.userUtil = userUtil;
    }

    @GetMapping
    public ResponseEntity<?> getAllInfo() {
        try {
            ArrayList<UserAndRoom> allInfo = (ArrayList<UserAndRoom>) scoreBoardService.getAllScoreInfo();
            long score = 0L;
            long myScore = 0L;
            long myGame = 0L;
            for (UserAndRoom userAndRoom : allInfo) {
                if (userAndRoom.getScore() == null) continue;
                if (userAndRoom.getUser() == userUtil.getLoggedInUser()) {
                    myScore += userAndRoom.getScore();
                    myGame += 1L;
                }
                score += userAndRoom.getScore();
            }
            HashMap<String, Long> scoreBoard = new HashMap<>();
            scoreBoard.put("전체 평점", score / allInfo.size()); // 사람들의 모든 평균 점수
            if (myGame == 0L) {
                scoreBoard.put("나의 평점", myScore);
            } else {
                scoreBoard.put("나의 평점", myScore / myGame);
            }

            ArrayList<Object> result = new ArrayList<>(allInfo);
            result.add(scoreBoard);

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
