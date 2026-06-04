package cz.vsb.fei.java2.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "players")
@Getter @Setter @NoArgsConstructor @ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    private LocalDateTime createdAt;

    public Player(String nickname) {
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }
}
