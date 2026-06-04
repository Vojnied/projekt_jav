package lab;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import lab.score.ScoreRepository;
import lab.score.ScoreException;

public class GameController {

    @FXML
    private Canvas canvas;

    @FXML
    private Label hits;

    @FXML
    private Label health;

    private DrawingThread timer;
    private World world;
    private App app;

    private int hitCount = 0;
    private int healthCount = 4;
    private double difficultyIncrement = 2;

    private long playerId;
    private long sessionId;
    private int currentLevel = 1;

    @FXML
    void fire() {
        if (!world.getCannon().didFire()) {
            Bullet bullet = new Bullet(world, world.getCannon().getPosition().add(15, 15));
            world.add(bullet);
            bullet.addHitListener(this::increaseHits);
            world.getCannon().setFired(true);
            timer.setAccumulatorGun(0.3);
        }
    }

    private void updateHits() {
        hits.setText(String.format("%03d", hitCount));
    }

    private void updateHealth() {
        Cannon cannon = world.getCannon();
        if (!cannon.isInvincible()) {
            healthCount--;
            health.setText(String.format("%01d", healthCount));
            if (healthCount <= 0) {
                gameOver(ScoreRepository.CAUSE_CENTIPEDE);
            }
            cannon.setInvincible(true);
            timer.setAccumulatorInvincible(2.0);
        }
    }

    private void increaseHits() {
        hitCount++;
        updateHits();
    }

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
        world = new World(canvas.getWidth(), canvas.getHeight());
        world.addPlayerHitListener(() -> {
            this.updateHealth();
        });
        System.out.println("GameController initialized");
    }

    private void gameOver(String cause) {
        timer.stop();
        try {
            if (sessionId > 0) {
                ScoreRepository.endSession(sessionId, cause);
            }
            app.switchToEndScreen(hitCount, playerId, sessionId, currentLevel);
        } catch (ScoreException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        try {
            app.switchToMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame(App app, long playerId, long sessionId) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        DrawingThread thread = new DrawingThread(canvas, world, this);
        timer = thread;
        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> world.moveLeft(true);
                case D -> world.moveRight(true);
                case W -> world.moveUp(true);
                case S -> world.moveDown(true);
                case SPACE -> this.fire();
                case ESCAPE -> this.gameOver(ScoreRepository.CAUSE_QUIT);
            }
        });

        canvas.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> world.moveLeft(false);
                case D -> world.moveRight(false);
                case W -> world.moveUp(false);
                case S -> world.moveDown(false);
            }
        });
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        this.app = app;
        timer.start();
    }

    public void increaseDifficulty() {
        currentLevel++;
        world.addCentipede(new Centipede(world, new Point2D(0, 0), 5 + (int) difficultyIncrement, 0.4 * Math.pow(0.8, (difficultyIncrement / 2))));
        difficultyIncrement = difficultyIncrement + 2;
    }
}
