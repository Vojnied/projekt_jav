package lab;

import javafx.scene.image.Image;

public enum SegmentType {

    HEAD("centipede_head.png"),
    BODY("centipede_body.png"),
    TAIL("centipede_body.png");

    private final Image image;

    SegmentType(String resourcePath) {
        this.image = new Image(
            SegmentType.class.getResourceAsStream(resourcePath)
        );
    }

    public Image getImage() {
        return image;
    }
}
