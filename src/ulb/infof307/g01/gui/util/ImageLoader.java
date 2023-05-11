package ulb.infof307.g01.gui.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    Map<String, Image> cachedImages = new HashMap<>();

    public Image get(String path) {
        cachedImages.computeIfAbsent(path, Image::new);
        return cachedImages.get(path);
    }
}
