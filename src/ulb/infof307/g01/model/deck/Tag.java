package ulb.infof307.g01.model.deck;

import com.google.gson.annotations.Expose;

import java.util.UUID;
import java.util.Objects;

public class Tag {
    @Expose
    private final UUID id;
    @Expose
    private String name;
    @Expose
    private final String color;

    public Tag(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.color = "#000000";
    }

    public Tag(String name, String color) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.color = color;
    }

    public Tag(String name, UUID id, String color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBackgroundDark() {
        // Convert the hex string to an RGB color value
        int r = Integer.parseInt(color.substring(1, 3), 16);
        int g = Integer.parseInt(color.substring(3, 5), 16);
        int b = Integer.parseInt(color.substring(5, 7), 16);

        // Calculate the brightness value of the color
        double brightness = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

        // Return true if the brightness is less than 0.5 (i.e., the color is "dark")
        return brightness < 0.3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Tag other = (Tag) obj;
        return id.equals(other.id)
                && name.equals(other.name)
                && color.equals(other.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
