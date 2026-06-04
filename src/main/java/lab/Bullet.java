package lab;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Bullet extends WorldEntity implements Collisionable {
    private static final double SIZE = 20;
    private static final double SPEED = 600;
    private static final Image IMAGE = new Image(Bullet.class.getResourceAsStream("bullet.png"));

    private final List<HitListener> hitListeners = new ArrayList<>();

    public Bullet(World world, Point2D position) {
        super(world, position);
    }


    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(IMAGE, position.getX(), position.getY(), SIZE, SIZE);
    }

    @Override
    public void simulate(double deltaTime) {
        position = position.add(0, - SPEED *  deltaTime);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), SIZE, SIZE);
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Obstacle || another instanceof CentipedeSegment) {
            world.remove(this);
            fireBulletHit();
        }
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public boolean addHitListener(HitListener e) {
        return hitListeners.add(e);
    }

    public boolean removeHitListener(HitListener o) {
        return hitListeners.remove(o);
    }

    private void fireBulletHit() {
        for (HitListener hitListener : hitListeners) {
            hitListener.bulletHit();
        }
    }

}
