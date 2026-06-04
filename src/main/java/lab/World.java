package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class World {

    private final double width;
    private final double height;

    private final List<DrawableSimulable> entities = new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();

    private final List<Centipede> centipedes = new ArrayList<>();
    private final Collection<Centipede> centipedesToAdd = new LinkedList<>();
    private final Collection<Centipede> centipedesToRemove = new LinkedList<>();

    private final List<PlayerHitListener> playerHitListeners = new ArrayList<>();

    private final Cannon cannon;

    public World(double width, double height) {
        this.width = width;
        this.height = height;

        cannon = new Cannon(this, new Point2D(width/2, height-50));
        add(cannon);

        Centipede centipede = new Centipede(this, new Point2D(0, 0), 5, 0.4);
        addCentipede(centipede);

        for (int i = 0; i < 30; i++) {
            entities.add(new Obstacle(this));
        }
    }

    /* ================= DRAW / SIMULATE ================= */

    public void draw(GraphicsContext gc) {
        gc.clearRect(0,0,width,height);

        // entity
        for (DrawableSimulable e : entities) {
            e.draw(gc);
        }

        // centipedes
        for (Centipede c : centipedes) {
            c.draw(gc);
        }
    }

    public void simulate(double deltaTime) {
        // entity
        for (DrawableSimulable e : entities) {
            e.simulate(deltaTime);
        }

        // centipedes
        for (Centipede c : centipedes) {
            c.simulate(deltaTime);
        }

        // entity x entity collisions
        for (DrawableSimulable e1 : entities) {
            if (e1 instanceof Collisionable c1) {
                for (DrawableSimulable e2 : entities) {
                    if (e2 instanceof Collisionable c2 && c1 != c2) {
                        if (c1.intersect(c2)) c1.hitBy(c2);
                    }
                }
            }
        }

        // entity x centipedes collisions
        for (Centipede c : centipedes) {

            for (DrawableSimulable e : entities) {
                if ((e instanceof Bullet || e instanceof Cannon) && c.intersect((Collisionable) e)) {
                    c.hitBy((Collisionable) e);
                }
            }
        }

        //cleanup
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        entitiesToRemove.clear();
        entitiesToAdd.clear();

        centipedes.removeAll(centipedesToRemove);
        centipedes.addAll(centipedesToAdd);
        centipedesToRemove.clear();
        centipedesToAdd.clear();

    }

    public void add(DrawableSimulable entity) {
        entitiesToAdd.add(entity);
    }

    public void remove(DrawableSimulable entity) {
        entitiesToRemove.add(entity);
    }

    public void addCentipede(Centipede c) {
        centipedesToAdd.add(c);
    }

    public void removeCentipede(Centipede c) {
        centipedesToRemove.add(c);
    }

    public void addPlayerHitListener(PlayerHitListener l) {
        playerHitListeners.add(l);
    }

    public void firePlayerHit() {
        for (PlayerHitListener l : playerHitListeners) {
            l.playerHit();
        }
    }

    public List<DrawableSimulable> getEntities() {
        return entities;
    }

    public List<Centipede> getCentipedes() {
        return centipedes;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public Cannon getCannon() { return cannon; }

    public void moveLeft(boolean moving) { cannon.setMoveLeft(moving); }
    public void moveRight(boolean moving) { cannon.setMoveRight(moving); }
    public void moveUp(boolean moving) { cannon.setMoveUp(moving); }
    public void moveDown(boolean moving) { cannon.setMoveDown(moving); }
}
