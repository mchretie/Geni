package ulb.infof307.g01.model.deck;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;

import java.lang.reflect.Type;
import java.util.*;


public class Deck implements Iterable<Card> {
    private UUID id;

    @Expose
    private String name;
    @Expose
    private String colorName;
    @Expose
    private final List<Card> cards;
    @Expose
    private final List<Tag> tags;
    @Expose
    private String color;
    @Expose
    private String image;
    @Expose
    private boolean isPublic;


    /* ====================================================================== */
    /*                             Constructors                               */
    /* ====================================================================== */

    public Deck(String name) {
        this(name,
                UUID.randomUUID());
    }

    public Deck(String name, UUID id) {
        this(name,
                id,
                new ArrayList<>(),
                new ArrayList<>());
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags) {
        this(name,
                id,
                cards,
                tags,
                "#00000000",
                "/backgrounds/default_background.jpg",
                "#000000",
                false);
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags, String color, String image, String colorName, boolean isPublic) {
        this.name = name;
        this.id = id;
        this.cards = cards;
        this.tags = tags;
        this.color = color;
        this.image = image;
        this.colorName = colorName;
        this.isPublic = isPublic;
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags, String color, String image, String colorName) {
        this(name, id, cards, tags, color, image, colorName, false);
    }

    public Deck(Deck deck) {
        this(deck.name, deck.id, deck.cards, deck.tags, deck.color, deck.image, deck.colorName, deck.isPublic);
    }


    /* ====================================================================== */
    /*                                Json methods                            */
    /* ====================================================================== */

    public static Deck fromJson(String json) {
        JsonDeserializer<Deck> deserializer
                = (jsonElement, type, jsonDeserializationContext) -> {

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray cardsJson = jsonObject.getAsJsonArray("cards");
            List<Card> cards = cardsFromJson(cardsJson);

            Type tagListType = new TypeToken<List<Tag>>(){}.getType();
            List<Tag> tags = new Gson().fromJson(jsonObject.get("tags"), tagListType);

            return new Deck(
                    jsonObject.get("name").getAsString(),
                    UUID.fromString(jsonObject.get("id").getAsString()),
                    cards,
                    tags,
                    jsonObject.get("color").getAsString(),
                    jsonObject.get("image").getAsString(),
                    jsonObject.get("colorName").getAsString(),
                    jsonObject.get("isPublic").getAsBoolean()
            );
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Deck.class, deserializer)
                .create();

        return gson.fromJson(json, Deck.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }


    /* ====================================================================== */
    /*                          Getters & Setters                             */
    /* ====================================================================== */

    public void switchOnlineVisibility() {
        this.isPublic = !this.isPublic;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    /**
     * Generate a new id for the deck and its cards
     * <p>
     * A new id is also generated for the cards because not doing so
     * can only lead to a broken database.
     * </p>
     */
    public void generateNewId() {
        this.id = UUID.randomUUID();
        for (Card card : cards) {
            card.setDeckId(this.id);
            card.generateNewId();
        }
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Card getCard(int index) throws IndexOutOfBoundsException {
        return cards.get(index);
    }

    public Card getFirstCard() throws IndexOutOfBoundsException {
        return getCard(0);
    }

    public Card getLastCard() throws IndexOutOfBoundsException {
        return getCard(cards.size() - 1);

    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public int cardCount() {
        return cards.size();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addCard(Card card) {
        card.setDeckId(id);
        cards.add(card);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public boolean tagExists(final String newTagName) {
        return tags.stream()
                .anyMatch(tag -> newTagName.equals(tag.getName()));
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DeckMetadata getMetadata() {
        return new DeckMetadata(id,
                name,
                isPublic,
                color,
                image,
                colorName,
                cards.size(),
                tags,
                hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Deck other = (Deck) obj;
        return this.id.equals(other.id)
                && this.name.equals(other.name)
                && this.cards.equals(other.cards)
                && this.tags.equals(other.tags)
                && this.color.equals(other.color)
                && this.image.equals(other.image)
                && this.colorName.equals(other.colorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags, color, image, colorName);
    }

    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public void setCardsFromJson(JsonArray cardsJson) {
        this.cards.clear();
        for (JsonElement card : cardsJson) {
            JsonObject cardObject = card.getAsJsonObject();
            String cardType = cardObject.get("cardType").getAsString();
            switch (cardType) {
                case "FlashCard" -> this.cards.add(new Gson().fromJson(card, FlashCard.class));
                case "MCQCard" -> this.cards.add(new Gson().fromJson(card, MCQCard.class));
                case "InputCard" -> this.cards.add(new Gson().fromJson(card, InputCard.class));
                default -> throw new IllegalStateException("Unexpected value: " + cardType);
            }
        }
    }

    public static List<Card> cardsFromJson(JsonArray cardsJson) {
        List<Card> cardsList = new ArrayList<>();
        for (JsonElement card : cardsJson) {
            JsonObject cardObject = card.getAsJsonObject();
            String cardType = cardObject.get("cardType").getAsString();
            switch (cardType) {
                case "FlashCard" -> cardsList.add(new Gson().fromJson(card, FlashCard.class));
                case "MCQCard" -> cardsList.add(new Gson().fromJson(card, MCQCard.class));
                case "InputCard" -> cardsList.add(new Gson().fromJson(card, InputCard.class));
                default -> throw new IllegalStateException("Unexpected value: " + cardType);
            }
        }
        return cardsList;
    }
}
