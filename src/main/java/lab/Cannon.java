package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class Cannon extends WorldEntity  implements Collisionable{

    private static final double WIDTH = 50;
    private static final double HEIGHT = 50;
    private static final double SPEED = 300;
    private static final Image IMAGE = new Image(Cannon.class.getResourceAsStream("ship.png"));
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean invincible = false;
    private boolean fired = false;


    public Cannon(World world, Point2D position) {
        super(world, position);
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(IMAGE, getPosition().getX(), getPosition().getY(), WIDTH, HEIGHT);
        if (invincible) {
            gc.setStroke(Color.RED);
            gc.strokeRect(getPosition().getX(), getPosition().getY(), WIDTH, HEIGHT);
        }
    }

	@Override
	public void simulate(double deltaT) {
        double vx = 0;
        double vy = 0;
        if (moveLeft) { vx-=SPEED; }
        if (moveRight) { vx+=SPEED; }
        if (moveUp) { vy-=SPEED; }
        if (moveDown) { vy+=SPEED; }
        position = position.add(deltaT*vx, deltaT*vy);
        position = new Point2D(Math.clamp(position.getX(), 0, world.getWidth() - WIDTH), Math.clamp(position.getY(), world.getHeight() * 0.8, world.getHeight() - HEIGHT));

	}

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), WIDTH, HEIGHT);
    }

    @Override
    public void hitBy(Collisionable another) {}

    @Override
    public boolean intersect(Collisionable another) {
        return false;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }
    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }
    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }
    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }
    public boolean isInvincible() {
        return invincible;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }
    public boolean didFire() {
        return fired;
    }

}
