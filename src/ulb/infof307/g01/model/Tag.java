package ulb.infof307.g01.model;

import java.util.UUID;

public class Tag {
    private String name;
    private UUID id = UUID.randomUUID();

    public Tag(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public UUID getId() { return id; }

    public void setName(String name) { this.name = name; }
}
