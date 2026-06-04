package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Obstacle extends WorldEntity implements Collisionable{
    private static final Random RANDOM = new Random();
    private static final int CELL_SIZE = 30;
    private static final List<Image> IMAGES = List.of(
        new Image(Obstacle.class.getResourceAsStream("block1.png")),
        new Image(Obstacle.class.getResourceAsStream("block2.png")
        ));
    private int health = 1;

    public Obstacle(World world) {
        this(world,
                new Point2D(
                    randomGridX(world),
                    randomGridY(world)));
    }

    public Obstacle(World world, Point2D position) {
        super(world, position);
    }

    private static double randomGridX(World world) {
        int columns = (int)(world.getWidth() / CELL_SIZE);
        int col = RANDOM.nextInt(columns);
        return (double)col * CELL_SIZE;
    }

    private static double randomGridY(World world) {
        int rows = (int)(world.getHeight() * 0.8 / CELL_SIZE);
        int row = RANDOM.nextInt(rows);
        return row * CELL_SIZE;
    }


    @Override
    public void drawInternal(GraphicsContext gc) {
        if (health < 0) return;
        gc.drawImage(IMAGES.get(health), (int) position.getX(), (int) position.getY(), CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void simulate(double deltaTime) {
        // doesnt move
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), CELL_SIZE, CELL_SIZE);
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Bullet) {
            health--;
            if (health < 0) world.remove(this);
        }
    }
}
