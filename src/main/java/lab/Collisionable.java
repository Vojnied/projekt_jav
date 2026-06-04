package lab;

import javafx.geometry.Rectangle2D;

public interface Collisionable {

	Rectangle2D getBoundingBox();

	boolean intersect(Collisionable another);

	void hitBy(Collisionable another);

}
