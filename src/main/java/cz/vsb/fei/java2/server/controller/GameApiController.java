package cz.vsb.fei.java2.server.controller;

import cz.vsb.fei.java2.server.entity.*;
import cz.vsb.fei.java2.server.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameApiController {

    private final GameService service;

    @PostMapping("/players")
    public Player findOrCreatePlayer(@RequestBody Map<String, String> body) {
        return service.findOrCreatePlayer(body.get("nickname"));
    }

    @PostMapping("/sessions")
    public GameSession createSession(@RequestBody Map<String, Long> body) {
        return service.createSession(body.get("playerId"));
    }

    @PutMapping("/sessions/{id}/end")
    public GameSession endSession(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.endSession(id, CauseOfDeath.valueOf(body.get("causeOfDeath")));
    }

    @PostMapping("/scores")
    public ScoreEntity saveScore(@RequestBody Map<String, Object> body) {
        return service.saveScore(
            (Integer) body.get("points"),
            Long.valueOf(body.get("playerId").toString()),
            Long.valueOf(body.get("sessionId").toString()),
            (Integer) body.get("levelNumber")
        );
    }

    @GetMapping("/scores")
    public List<ScoreRecord> getLeaderboard() {
        return service.getLeaderboard();
    }

    @GetMapping("/scores/above")
    public List<ScoreRecord> getScoresAbove(@RequestParam int min) {
        return service.getScoresAbove(min);
    }

    @DeleteMapping("/scores/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        service.deleteScore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/difficulties")
    public List<DifficultyLevel> getDifficulties() {
        return service.getAllDifficulties();
    }
}
