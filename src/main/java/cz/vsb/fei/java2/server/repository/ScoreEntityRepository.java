package cz.vsb.fei.java2.server.repository;

import cz.vsb.fei.java2.server.entity.ScoreEntity;
import cz.vsb.fei.java2.server.entity.ScoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScoreEntityRepository extends JpaRepository<ScoreEntity, Long> {
    @Query("""
        SELECT new cz.vsb.fei.java2.server.entity.ScoreRecord(
            s.id, s.points, p.nickname, s.achievedAt
        )
        FROM ScoreEntity s JOIN s.player p
        ORDER BY s.points DESC
        """)
    List<ScoreRecord> findAllByOrderByPointsDesc();

    @Query("""
        SELECT new cz.vsb.fei.java2.server.entity.ScoreRecord(
            s.id, s.points, p.nickname, s.achievedAt
        )
        FROM ScoreEntity s JOIN s.player p
        WHERE s.points >= :minPoints
        ORDER BY s.points DESC
        """)
    List<ScoreRecord> findScoresAbove(@Param("minPoints") int minPoints);
}
