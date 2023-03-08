package ulb.infof307.g01.model;

import java.util.UUID;

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
}
