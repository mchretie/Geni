package ulb.infof307.g01.model;

import java.util.*;


public class Deck implements Iterable<Card> {
    private String name;
    private final UUID id;
    private final List<Card> cards;
    private final List<Tag> tags;

    public Deck(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.cards = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Deck(String name, UUID id) {
        this.name = name;
        this.id = id;
        this.cards = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags) {
        this.name = name;
        this.id = id;
        this.cards = cards;
        this.tags = tags;
    }

    public Deck(Map map) {
        this.name = (String) map.get("name");
        this.id = UUID.fromString((String) map.get("id"));
        this.cards = new ArrayList<>();
        this.tags = new ArrayList<>();
//          TODO finish implementation by reading cards and tags
//        example of map:
//        "deck_cards": [
//       {
//           "card_id": "599f4ff1-4913-41a6-88c5-078a9b488535",
//           "card_front": "Front",
//           "card_back": "Back"
//       }
//   ],
//   "deck_tags": [
//       {
//           "tag_id": "699f4ff1-4913-41a6-88c5-078a9b488535",
//           "tag_name": "je suis un tag",
//           "tag_color": "#000000"
//       }
//   ]

    }

    public List<Tag> getTags() {
        return tags;
    }

    public Card getCard(int index) throws IndexOutOfBoundsException {
        return cards.get(index);
    }

    public Card getLastCard() throws IndexOutOfBoundsException {
        return getCard(cards.size() - 1);

    }

    public Card getFirstCard() throws IndexOutOfBoundsException {
        return getCard(0);
    }

    public List<Card> getCards() {
        return cards;
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

    public void addCards(List<Card> cards) {
        cards.forEach(this::addCard);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void removeCard(Card card) {
        cards.remove(card);
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
                && this.tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags);
    }

    public Iterator<Card> iterator() {
        return cards.iterator();

    }

    public String toJson() {
//        TODO: implement toJson method using Gson
//        return should be something like:
//        {
//            "deck_id": "399f4ff1-4913-41a6-88c5-078a9b488532",
//                "deck_name": "ta grand mère",
//                "deck_cards": [
//            {
//                "card_id": "599f4ff1-4913-41a6-88c5-078a9b488535",
//                    "card_front": "Front",
//                    "card_back": "Back"
//            }
//   ],
//            "deck_tags": [
//            {
//                "tag_id": "699f4ff1-4913-41a6-88c5-078a9b488535",
//                    "tag_name": "je suis un tag",
//                    "tag_color": "#000000"
//            }
//   ]
//        }

        return "";
    }
}