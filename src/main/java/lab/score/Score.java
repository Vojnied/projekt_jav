package lab.score;

import lombok.Data;

@Data
public class Score {
    private long id;
    private String nickName;
    private int score;

    public Score(long id, String nickName, int score) {
        this.id = id;
        this.nickName = nickName;
        this.score = score;
    }

    public Score(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }
}
