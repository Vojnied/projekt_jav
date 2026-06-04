package lab;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface DrawableSimulable {
    void draw(GraphicsContext gc);

    void simulate(double deltaTime);
    Rectangle2D getBoundingBox();
}
