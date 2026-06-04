package lab;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lab.score.Score;
import lab.score.ScoreException;
import lab.score.ScoreRepository;

public class MenuController {

    private App app;

    @FXML
    private BorderPane menuPanel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button playButton;

    @FXML
    private TextField nickField;

    @FXML
    private TableColumn<Score, String> columnNickName;

    @FXML
    private TableColumn<Score, Integer> columnScore;

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    void onBtnDelete(ActionEvent event) {
        try {
            Score score = scoreTable.getSelectionModel().getSelectedItem();
            ScoreRepository.delete(score);
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Storing problem", e);
        }
    }

    private static void showAlertDialog(Alert.AlertType alertType, String text, Exception e) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(text);
        alert.getDialogPane().setContentText(e.getMessage());
        e.printStackTrace();
        alert.showAndWait();
    }

    private void updateTable() {
        scoreTable.getItems().clear();
        try {
            scoreTable.getItems().addAll(ScoreRepository.load());
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Loading problem", e);
        }
        deleteButton.setDisable(scoreTable.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    void onBtnPlay(ActionEvent event) {
        try {
            String nick = nickField.getText().trim();
            if (nick.isEmpty()) {
                showAlertDialog(Alert.AlertType.WARNING, "Missing nickname", new Exception("Please enter a nickname before playing."));
                return;
            }

            long playerId = ScoreRepository.findOrCreatePlayer(nick);
            long sessionId = ScoreRepository.createSession(playerId);
            app.switchToGame(playerId, sessionId);
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Program run error. Seriouse error ocured. ", e);
        }
    }

    @FXML
    void initialize() {
        assert menuPanel != null : "fx:id=\"menuPanel\" was not injected: check your FXML file 'menu.fxml'.";
        assert deleteButton != null : "fx:id=\"onBtnSave\" was not injected: check your FXML file 'menu.fxml'.";
        assert playButton != null : "fx:id=\"playButton\" was not injected: check your FXML file 'menu.fxml'.";
        assert nickField != null : "fx:id=\"nickField\" was not injected: check your FXML file 'menu.fxml'.";
        columnNickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        updateTable();
        scoreTable.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> deleteButton.setDisable(newValue == null));
    }

    public void setApp(App app) {
        this.app = app;
    }
}
