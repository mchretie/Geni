package ulb.infof307.g01.model.card.visitor;

import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;

public interface CardVisitor {
    void visit(FlashCard flashCard);
    void visit(MCQCard multipleChoiceCard);
    void visit(InputCard inputCard);
}
