package Client.Model;

import javafx.scene.image.ImageView;

public class Image extends ImageView {
    private final String path;
    private final String name;

    public Image(String path, double height, double width, String name) {
        this.path = path;
        this.name = name;
        super.setImage(new javafx.scene.image.Image(path));
        super.setFitHeight(height);
        super.setFitWidth(width);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
