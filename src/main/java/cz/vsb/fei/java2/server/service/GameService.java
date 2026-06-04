package cz.vsb.fei.java2.server.service;

import cz.vsb.fei.java2.server.entity.*;
import cz.vsb.fei.java2.server.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final PlayerRepository playerRepo;
    private final ScoreEntityRepository scoreRepo;
    private final GameSessionRepository sessionRepo;
    private final DifficultyLevelRepository diffRepo;

    public Player findOrCreatePlayer(String nickname) {
        return playerRepo.findByNickname(nickname)
            .orElseGet(() -> playerRepo.save(new Player(nickname)));
    }

    public GameSession createSession(Long playerId) {
        Player player = playerRepo.findById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));
        GameSession session = new GameSession();
        session.setStartedAt(LocalDateTime.now());
        session.setPlayer(player);
        return sessionRepo.save(session);
    }

    public GameSession endSession(Long sessionId, CauseOfDeath causeOfDeath) {
        GameSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
        session.setEndedAt(LocalDateTime.now());
        session.setCauseOfDeath(causeOfDeath);
        return sessionRepo.save(session);
    }

    public ScoreEntity saveScore(int points, Long playerId, Long sessionId, int levelNumber) {
        Player player = playerRepo.findById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));
        GameSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
        DifficultyLevel diff = diffRepo.findByLevelNumber(levelNumber)
            .orElseThrow(() -> new IllegalArgumentException("Difficulty not found: " + levelNumber));

        ScoreEntity score = new ScoreEntity();
        score.setPoints(points);
        score.setAchievedAt(LocalDateTime.now());
        score.setPlayer(player);
        score.setSession(session);
        score.setDifficultyLevel(diff);
        return scoreRepo.save(score);
    }

    public List<ScoreRecord> getLeaderboard() {
        return scoreRepo.findAllByOrderByPointsDesc();
    }

    public List<ScoreRecord> getScoresAbove(int minPoints) {
        return scoreRepo.findScoresAbove(minPoints);
    }

    public void deleteScore(Long id) {
        scoreRepo.deleteById(id);
    }

    public List<DifficultyLevel> getAllDifficulties() {
        return diffRepo.findAll();
    }
}
