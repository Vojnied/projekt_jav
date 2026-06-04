package cz.vsb.fei.java2.server.entity;

import java.time.LocalDateTime;

public record ScoreRecord(Long id, int points, String nickname, LocalDateTime achievedAt) {
}
