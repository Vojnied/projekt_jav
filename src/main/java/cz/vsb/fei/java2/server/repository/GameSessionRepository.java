package cz.vsb.fei.java2.server.repository;

import cz.vsb.fei.java2.server.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}
