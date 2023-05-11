package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.card.visitor.CardVisitor;
import ulb.infof307.g01.server.database.DatabaseAccess;

public class CardDAO extends DAO implements CardVisitor {

    private final DatabaseAccess database;

    public CardDAO(DatabaseAccess database) {
        this.database = database;
    }

    @Override
    public void visit(FlashCard flashCard) {
        String upsertFlashCard = """
                INSERT INTO flash_card (card_id, back)
                VALUES (?, ?)
                ON CONFLICT(card_id)
                DO UPDATE SET back = ?
                """;

        database.executeUpdate(upsertFlashCard,
                               flashCard.getId().toString(),
                               flashCard.getBack(),
                               flashCard.getBack());
    }

    @Override
    public void visit(MCQCard multipleChoiceCard) {
        String upsertMCQCard = """
                INSERT INTO mcq_card (card_id, correct_answer_index, countdown_time)
                VALUES (?, ?, ?)
                ON CONFLICT(card_id)
                DO UPDATE SET correct_answer_index = ?, countdown_time = ?
                """;

        database.executeUpdate(upsertMCQCard,
                               multipleChoiceCard.getId().toString(),
                               multipleChoiceCard.getCorrectChoiceIndex(),
                               multipleChoiceCard.getCountdownTime(),
                               multipleChoiceCard.getCorrectChoiceIndex(),
                               multipleChoiceCard.getCountdownTime()
        );

        String upsertMCQCardAnswer = """
                INSERT INTO mcq_answer (card_id, answer, answer_index)
                VALUES (?, ?, ?)
                ON CONFLICT(card_id, answer_index)
                DO NOTHING
                """;

        for (int i = 0; i < multipleChoiceCard.getChoicesCount(); i++)
            database.executeUpdate(upsertMCQCardAnswer,
                                   multipleChoiceCard.getId().toString(),
                                   multipleChoiceCard.getChoice(i),
                                   i);
    }

    @Override
    public void visit(InputCard inputCard) {

        String upsertInputCard = """
                INSERT INTO input_card (card_id, answer, countdown_time)
                VALUES (?, ?, ?)
                ON CONFLICT(card_id)
                DO UPDATE SET answer = ? , countdown_time = ?
                """;

        database.executeUpdate(upsertInputCard,
                               inputCard.getId().toString(),
                               inputCard.getAnswer(),
                               inputCard.getCountdownTime(),
                               inputCard.getAnswer(),
                               inputCard.getCountdownTime()
        );
    }
}
