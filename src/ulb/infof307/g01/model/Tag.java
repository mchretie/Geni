package ulb.infof307.g01.model;

import java.util.UUID;
import java.util.Objects;

public class Tag {
    private String name;
    private UUID id;

    private String color;

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

    public String getName() { return name; }

    public String getColor() { return color; }
    public UUID getId() { return id; }

    public void setName(String name) { this.name = name; }

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
