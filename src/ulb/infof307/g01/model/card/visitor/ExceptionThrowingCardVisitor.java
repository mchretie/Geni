package ulb.infof307.g01.model.card.visitor;

import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;

public interface ExceptionThrowingCardVisitor<E extends Exception> {
    void visit(FlashCard flashCard) throws E;
    void visit(MCQCard multipleChoiceCard) throws E;
    void visit(InputCard inputCard) throws E;
}
