package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lab.score.ScoreException;
import lab.score.ScoreRepository;

public class EndScreenController {

    @FXML
    private Label scoreLabel;

    private int score;
    private long playerId;
    private long sessionId;
    private int levelNumber;
    private App app;

    public void init(int score, App app, long playerId, long sessionId, int levelNumber) {
        this.score = score;
        this.app = app;
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.levelNumber = levelNumber;
        scoreLabel.setText("Your score: " + score);
    }

    @FXML
    void onSave() {
        try {
            ScoreRepository.saveScore(score, playerId, sessionId, levelNumber);
            app.switchToMenu();
        } catch (ScoreException e) {
            showAlert("Saving score failed", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

}
