package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.util.List;
import java.util.UUID;

public class TagManager {

    private static TagManager tm;

    public static TagManager singleton(){
        if (tm == null){
            tm = new TagManager();
        }
        return tm;
    }
    public Tag getTag(UUID uuid){
        return null;
    }

    public List<Tag> getTagsFor(UUID deckUuid){
        return null;
    }

    public List<Tag> getAllTags(){
        return null;
    }

    public void addTag(Deck deck, Tag tag){
        return;
    }

    public void delTag(Tag tag){
        return;
    }

}
