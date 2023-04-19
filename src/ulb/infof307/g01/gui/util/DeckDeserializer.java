package ulb.infof307.g01.gui.util;

import com.google.gson.*;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.Deck;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeckDeserializer implements JsonDeserializer<Deck> {
    @Override
    public Deck deserialize(JsonElement jsonElement,
                            Type type,
                            JsonDeserializationContext context)

            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray cardsArray = jsonObject.getAsJsonArray("cards");

        Deck deck = new Deck(jsonObject);
        List<Card> cards = new ArrayList<>();

        for (JsonElement cardElement : cardsArray) {
            JsonObject cardObject = cardElement.getAsJsonObject();

            // Determine the type of card based on properties of the card object
            Card card;
            String cardType = cardObject.get("cardType").getAsString();
            card = switch (cardType) {
                case "FlashCard" ->
                        context.deserialize(cardElement, FlashCard.class);
                case "InputCard" ->
                        context.deserialize(cardElement, InputCard.class);
                case "MCQCard" ->
                        context.deserialize(cardElement, MCQCard.class);

                default -> throw new JsonParseException("Unknown card type");
            };

            cards.add(card);
        }

        deck.setCards(cards);

        return deck;
    }
}
