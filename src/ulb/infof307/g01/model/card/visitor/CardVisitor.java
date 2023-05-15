package ulb.infof307.g01.model.card.visitor;

import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;

public interface CardVisitor<T extends Throwable> {
    void visit(FlashCard flashCard) throws T;
    void visit(MCQCard multipleChoiceCard) throws T;
    void visit(InputCard inputCard) throws T;
}
