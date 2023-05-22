package ulb.infof307.g01.gui.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private final Map<String, Image> cachedImages = new HashMap<>();

    public Image get(String path) {
        if (!cachedImages.containsKey(path)) {
            cachedImages.put(path, new Image(path));
        }
        return cachedImages.get(path);
    }
}
