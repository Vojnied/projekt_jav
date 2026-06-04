package lab;

import java.io.IOException;
import java.net.URL;
import cz.vsb.fei.java2.server.SpringBootApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.SpringApplication;

public class App extends Application {

    private GameController gameController;
    private MenuController menuController;
    private EndScreenController endScreenController;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        try {
            SpringApplication.run(SpringBootApp.class);
            System.out.println("═══════════════════════════════════════════");
            System.out.println("  H2 Console: http://localhost:8080/h2-console");
            System.out.println("  JDBC URL:   jdbc:h2:file:./data/centipede");
            System.out.println("  User:       sa");
            System.out.println("  Password:   (blank)");
            System.out.println("═══════════════════════════════════════════");
        } catch (Exception e) {
            System.err.println("FATAL: Spring Boot failed to start: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Font.loadFont(this.getClass().getResourceAsStream("/TRON.TTF"), 20);

            this.primaryStage = primaryStage;
            primaryStage.setTitle("Centipede");

            switchToMenu();
            primaryStage.show();
            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToGame(long playerId, long sessionId) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();
        gameController = gameLoader.getController();
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gameController.startGame(this, playerId, sessionId);
    }

    public void switchToMenu() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }

    public void switchToEndScreen(int score, long playerId, long sessionId, int levelNumber) throws IOException {
        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("/lab/endScreen.fxml"));
        Parent root = endLoader.load();
        endScreenController = endLoader.getController();
        endScreenController.init(score, this, playerId, sessionId, levelNumber);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    @Override
    public void stop() throws Exception {
        if (gameController != null) {
            gameController.stop();
        }
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
