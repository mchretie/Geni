package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// clang-format off
public class CardManager {

    private static CardManager cm;
    private final Database db = Database.singleton();

    public static CardManager singleton() {
        if (cm == null) {
            cm = new CardManager();
        }
        return cm;
    }

    public Card getCard(UUID uuid)
            throws CardNotExistsException {
        String sql = """
                    SELECT card_id, front, back
                    FROM card
                    WHERE card_id = '%s'
                """.formatted(uuid.toString());

        Card card = null;

        try {
            ResultSet res = db.executeQuery(sql);
            if (!res.next())
                throw new CardNotExistsException("Card with id(%s) doesn’t exist"
                        .formatted(uuid.toString()));
            card = getCardFromResultSet(res);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return card;
    }

    /**
     * <p>
     * If the deck_id is incorrect, the returned list will be empty.
     */
    public List<Card> getCardsFrom(UUID deckUuid) {
        String sql = """
                    SELECT DISTINCT card.card_id, card.front, card.back
                    FROM card, deck_card
                    WHERE deck_card.card_id = card.card_id AND deck_card.deck_id = '%s'
                """.formatted(deckUuid.toString());

        List<Card> cards = new ArrayList<Card>();

        try {
            ResultSet res = db.executeQuery(sql);
            while (res.next()) {
                cards.add(getCardFromResultSet(res));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cards;
    }

    public void delCard(Card card) {
        String sql = """
                    DELETE FROM card
                    WHERE card.card_id = '%s'
                """.formatted(card.getId().toString());

        try {
            db.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateCard(Card card) {
        String sql = """
                    INSERT INTO card (card_id, front, back)
                    VALUES ('%1$s', '%2$s', '%3$s')
                    ON CONFLICT(card_id)
                    DO UPDATE SET front = '%2$s', back = '%3$s'
                """.formatted(
                card.getId().toString(),
                card.getFront(),
                card.getBack());

        try {
            db.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private Card getCardFromResultSet(ResultSet res)
            throws SQLException {
        return new Card(
                UUID.fromString(res.getString("card_id")),
                res.getString("front"),
                res.getString("back"));
    }
}
