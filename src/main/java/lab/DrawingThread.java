package lab;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final World world;
    private final GameController controller;

    private long lastFrame = 0;
    private double deltaTime = 0;
    private double accumulatorInvincible = 0;
    private double accumulatorGun = 0;

    public DrawingThread(Canvas canvas, World world, GameController controller) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.world = world;
        this.controller = controller;
    }

    @Override
    public void handle(long now) {
        double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
        lastFrame = now;
        deltaTime += delta;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.world.draw(gc);
        this.world.simulate(delta);
        if (this.world.getCentipedes().isEmpty()){
            controller.increaseDifficulty();
        }
        if (world.getCannon().isInvincible()){
            this.checkCannon(delta);
        }
        if (world.getCannon().didFire()){
            this.checkGun(delta);
        }

    }

    public double getDelta() {
        return deltaTime;
    }

    private void checkCannon(double delta) {
        accumulatorInvincible -= delta;
        if (accumulatorInvincible <= 0) {
            accumulatorInvincible = 0;
            world.getCannon().setInvincible(false);
        }
    }

    public void setAccumulatorInvincible(double inviTime) {
        accumulatorInvincible = inviTime;
    }

    private void checkGun(double delta) {
        accumulatorGun -= delta;
        if (accumulatorGun <= 0) {
            accumulatorGun = 0;
            world.getCannon().setFired(false);
        }
    }

    public void setAccumulatorGun(double gunTime) {
        accumulatorGun = gunTime;
    }
}
