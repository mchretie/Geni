package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.*;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Save and retrieve decks from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Deck, apply changes
 * to it before saving it with saveDeck or deleteDeck.
 * <p>
 * Do not use directly, use the Database facade instead.
 *
 * @see ulb.infof307.g01.server.database.Database
 */
public class DeckDAO extends DAO {

    private final DatabaseAccess database;
    private TagDAO tagDao;
    private CardDAO cardDao;

    public DeckDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void setTagDao(TagDAO tagDao) {
        this.tagDao = tagDao;
    }

    public void setCardDao(CardDAO cardDao) {
        this.cardDao = cardDao;
    }

    /**
     * <p>
     * A deck is invalid if there exists a deck with
     * the same name but a different id in the database.
     * <p>
     * This may happen when a deck created outside
     * this class has the same name as one in the database.
     * This can be avoided by checking for uniqueness
     * beforehand.
     *
     * @see DeckDAO#deckNameExists
     */
    public boolean isDeckValid(Deck deck, UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id, name
                FROM deck
                WHERE NOT deck_id = ? AND user_id = ? AND name = ?
                """;

        return !checkedNext(database.executeQuery(sql,
                deck.getId().toString(),
                userId.toString(),
                deck.getName()));
    }

    public boolean deckNameExists(String name, UUID userId) throws DatabaseException {
        String sql = """
                SELECT name
                FROM deck
                WHERE name = ? AND user_id = ?
                """;

        return checkedNext(database.executeQuery(sql, name, userId.toString()));
    }


    /**
     * Write to long-term memory the contents of a deck
     * <p>
     * If the given deck is not valid, it will be ignored.
     *
     * @see DeckDAO#isDeckValid
     */
    public void saveDeck(Deck deck, UUID userId) throws DatabaseException {
        if (!isDeckValid(deck, userId))
            return;
        saveDeckIdentity(deck, userId);
        saveDeckTags(deck);
        saveDeckCards(deck);
    }

    public Deck getDeck(UUID deckId) throws DatabaseException {
        String sql = """
                SELECT d.*,
                       CASE WHEN m.deck_id IS NOT NULL THEN 1 ELSE 0 END AS public
                FROM deck d
                         LEFT JOIN marketplace m ON d.deck_id = m.deck_id
                WHERE d.deck_id = ?;
                """;

        ResultSet res = database.executeQuery(sql,
                deckId.toString());
        if (!checkedNext(res))
            return null;
        return extractDeckFrom(res);
    }

    /**
     * Get multiple decks from their ids
     * <p>
     *
     * @param res result from a query on deck table
     * @return List of decks
     */
    public List<Deck> getDecks(List<UUID> res) throws DatabaseException {
        List<Deck> decks = new ArrayList<>();
        for (UUID deckId : res)
            decks.add(getDeck(deckId));
        return decks;
    }

    /**
     * Get all decks present in the database
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllDecks() throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                """;

        ResultSet res = database.executeQuery(sql);
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");
        return getDecks(deckIds);
    }

    /**
     * Get all decks associated with given user
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<DeckMetadata> getAllUserDecksMetadata(UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM user_deck_collection
                WHERE user_id = ?
                """;


        ResultSet res = database.executeQuery(sql, userId.toString());
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");
        return extractDeckMetadata(getDecks(deckIds));
    }

    public UUID getDeckOwnerId(UUID deckId) throws DatabaseException {
        try {
            String sql = """
                    SELECT user_id
                    FROM deck
                    WHERE deck_id = ?
                    """;
            ResultSet res = database.executeQuery(sql, deckId.toString());
            return UUID.fromString(res.getString("user_id"));
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Approximate search of decks with given search string
     *
     * @param userSearch query
     * @param userId     user id
     * @return List of decks
     */
    public List<Deck> searchDecks(String userSearch, UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                WHERE user_id = ? AND name LIKE ?
                """;

        String pattern = userSearch + "%";
        ResultSet res = database.executeQuery(sql, userId.toString(), pattern);
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");

        return getDecks(deckIds);
    }

    public List<DeckMetadata> searchDecksMetadata(String userSearch, UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                WHERE user_id = ? AND name LIKE ?
                """;

        String pattern = userSearch + "%";
        ResultSet res = database.executeQuery(sql, userId.toString(), pattern);
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");

        return extractDeckMetadata(getDecks(deckIds));
    }

    /**
     * Delete a deck along with its cards from the database.
     */
    public void deleteDeck(UUID deckId, UUID userId) throws DatabaseException {
        String sql = """
                DELETE FROM deck
                WHERE deck_id = ? and user_id = ?
                """;

        database.executeUpdate(sql, deckId.toString(), userId.toString());
    }


    /**
     * Update a deck’s name
     * <p>
     * Upon the saving of multiple decks with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    private void saveDeckIdentity(Deck deck, UUID userId) throws DatabaseException {
        String sql = """
                INSERT INTO deck (deck_id, user_id, name, color, image, color_name, public)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(deck_id)
                DO UPDATE SET name = ?, color = ?, image = ?, color_name = ?, public = ?
                """;

        database.executeUpdate(sql,
                deck.getId().toString(),
                userId.toString(),
                deck.getName(),
                deck.getColor(),
                deck.getImage(),
                deck.getColorName(),
                deck.isPublic() ? 1 : 0,

                deck.getName(),
                deck.getColor(),
                deck.getImage(),
                deck.getColorName(),
                deck.isPublic() ? 1 : 0
        );
    }

    private void saveDeckTags(Deck deck) throws DatabaseException {
        tagDao.saveTagsFor(deck);
    }

    /**
     * Update the database with the data from the deck
     */
    @SuppressWarnings("unchecked")
    private void saveDeckCards(Deck deck) throws DatabaseException {
        HashSet<Card> currentCards = new HashSet<>(getCardsFor(deck.getId()));
        HashSet<Card> newCards = new HashSet<>(deck.getCards());

        Set<Card> addedCards = (Set<Card>) newCards.clone();
        addedCards.removeAll(currentCards);

        Set<Card> deletedCards = (Set<Card>) currentCards.clone();
        deletedCards.removeAll(newCards);
        for (Card deletedCard : deletedCards)
            deleteCard(deletedCard);

        for (Card addedCard : addedCards) {
            saveCard(addedCard);
        }
    }

    private void saveCard(Card card) throws DatabaseException {
        String upsertCard = """
                INSERT INTO card (card_id, deck_id, front)
                VALUES (?, ?, ?)
                ON CONFLICT(card_id)
                DO UPDATE SET front = ?
                """;

        database.executeUpdate(upsertCard,
                card.getId().toString(),
                card.getDeckId().toString(),
                card.getFront(),
                card.getFront()
        );

        card.accept(cardDao);
    }

    private void deleteCard(Card card) throws DatabaseException {
        String sql = """
                DELETE FROM card
                WHERE card.card_id = ?
                """;

        database.executeUpdate(sql, card.getId().toString());
    }

    private FlashCard extractFlashCardFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("card_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            String front = res.getString("front");
            String back = res.getString("back");
            return new FlashCard(uuid, deckId, front, back);
        } catch (SQLException e) {
            throw new DatabaseException((e.getMessage()));
        }
    }

    private List<FlashCard> getFlashCardsFor(UUID deckUuid) throws DatabaseException {
        String sql = """
                SELECT card.card_id, deck_id, front, back
                FROM card
                INNER JOIN flash_card
                ON card.card_id = flash_card.card_id
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, deckUuid.toString());
        List<FlashCard> cards = new ArrayList<>();
        while (checkedNext(res))
            cards.add(extractFlashCardFrom(res));
        return cards;
    }

    private MCQCard extractMCQCardFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("card_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            String front = res.getString("front");
            Integer countdownTime = res.getInt("countdown_time");
            int correctAnswerIndex = Integer.parseInt(res.getString("correct_answer_index"));
            List<String> answers = getMCQAnswersFor(uuid);
            return new MCQCard(uuid, deckId, front, answers, correctAnswerIndex, countdownTime);
        } catch (SQLException e) {
            throw new DatabaseException((e.getMessage()));
        }
    }

    private List<String> getMCQAnswersFor(UUID cardUuid) throws DatabaseException, SQLException {
        String sql = """
                SELECT answer
                FROM mcq_answer
                WHERE card_id = ?
                ORDER BY answer_index ASC;
                """;

        ResultSet res = database.executeQuery(sql, cardUuid.toString());
        List<String> answers = new ArrayList<>();
        while (checkedNext(res))
            answers.add(res.getString("answer"));
        return answers;
    }

    private List<MCQCard> getMCQCardsFor(UUID deckUuid) throws DatabaseException {
        String sql = """
                SELECT card.card_id, deck_id, front, correct_answer_index, countdown_time
                FROM card
                INNER JOIN mcq_card
                ON card.card_id = mcq_card.card_id
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, deckUuid.toString());
        List<MCQCard> cards = new ArrayList<>();
        while (checkedNext(res))
            cards.add(extractMCQCardFrom(res));
        return cards;
    }

    private InputCard extractInputCardFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("card_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            String front = res.getString("front");
            Integer countdownTime = res.getInt("countdown_time");
            String answer = res.getString("answer");
            return new InputCard(uuid, deckId, front, answer, countdownTime);
        } catch (SQLException e) {
            throw new DatabaseException((e.getMessage()));
        }
    }

    private List<InputCard> getInputCardsFor(UUID deckUuid) throws DatabaseException {
        String sql = """
                SELECT card.card_id, deck_id, front, answer, countdown_time
                FROM card
                INNER JOIN input_card
                ON card.card_id = input_card.card_id
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, deckUuid.toString());
        List<InputCard> cards = new ArrayList<>();
        while (checkedNext(res))
            cards.add(extractInputCardFrom(res));
        return cards;
    }

    /**
     * Get all cards associated with given deck
     *
     * @return The cards of the deck. If the id
     * is not in the database, an empty list is
     * returned.
     */
    private List<Card> getCardsFor(UUID deckUuid) throws DatabaseException {
        List<Card> cards = new ArrayList<>();
        cards.addAll(getFlashCardsFor(deckUuid));
        cards.addAll(getMCQCardsFor(deckUuid));
        cards.addAll(getInputCardsFor(deckUuid));
        return cards;
    }

    public Deck extractDeckFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("deck_id"));
            String name = res.getString("name");
            String color = res.getString("color");
            String image = res.getString("image");
            String colorName = res.getString("color_name");
            boolean isPublic = res.getInt("public") == 1;
            List<Card> cards = getCardsFor(uuid);
            List<Tag> tags = tagDao.getTagsFor(uuid);

            return new Deck(name, uuid, cards, tags, color, image, colorName, isPublic);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private List<DeckMetadata> extractDeckMetadata(List<Deck> decks) {
        List<DeckMetadata> decksMetadata = new ArrayList<>();
        decks.forEach(d -> decksMetadata.add(d.getMetadata()));
        return decksMetadata;
    }
}
