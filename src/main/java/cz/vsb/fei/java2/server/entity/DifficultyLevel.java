package cz.vsb.fei.java2.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "difficulty_levels")
@Getter @Setter @NoArgsConstructor @ToString
public class DifficultyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int levelNumber;

    private int centipedeLength;
    private double stepTime;

    public DifficultyLevel(int levelNumber, int centipedeLength, double stepTime) {
        this.levelNumber = levelNumber;
        this.centipedeLength = centipedeLength;
        this.stepTime = stepTime;
    }
}
