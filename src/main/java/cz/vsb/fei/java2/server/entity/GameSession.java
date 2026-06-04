package cz.vsb.fei.java2.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "game_sessions")
@Getter @Setter @NoArgsConstructor @ToString
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private CauseOfDeath causeOfDeath;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
