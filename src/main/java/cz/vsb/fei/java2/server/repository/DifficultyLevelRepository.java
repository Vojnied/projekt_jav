package cz.vsb.fei.java2.server.repository;

import cz.vsb.fei.java2.server.entity.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevel, Long> {
    Optional<DifficultyLevel> findByLevelNumber(int levelNumber);
}
