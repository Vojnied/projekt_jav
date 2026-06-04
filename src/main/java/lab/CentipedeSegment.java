package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

public class CentipedeSegment extends WorldEntity implements Collisionable {

    public static final int CELL = 30;

    @Getter @Setter
    private SegmentType type;
    private Centipede centipede;

    public CentipedeSegment(
        World world,
        Point2D position,
        SegmentType type,
        Centipede centipede
    ) {
        super(world, position);
        this.type = type;
        this.centipede = centipede;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        double x = position.getX();
        double y = position.getY();

        gc.save();

        if (centipede.getDirX() == 1) {
            // opposite dir
            gc.translate(x + CELL, y);
            gc.scale(-1, 1);
            gc.drawImage(type.getImage(), 0, 0, CELL, CELL);
        } else {
            // normal
            gc.drawImage(type.getImage(), x, y, CELL, CELL);
        }

        gc.restore();
    }

    @Override
    public void simulate(double deltaTime) {
        // centipede controls movement
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), CELL, CELL);
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        // centipede handles collisions
    }

    public void setController(Centipede controller) {
        this.centipede = controller;
    }
}
