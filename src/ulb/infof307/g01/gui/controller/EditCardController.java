package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.FlashCard;

public class EditCardController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;
    private final ControllerListener controllerListener;
    private final Stage stage;
    private final DeckDAO deckDAO;
    private final Deck deck;
    private final Card card;
    private final boolean front;
    private final ErrorHandler errorHandler;

    public EditCardController(Stage stage,
                              Deck deck,
                              Card card,
                              boolean front,
                              DeckDAO deckDAO,
                              ErrorHandler errorHandler,
                              MainWindowViewController mainWindowViewController,
                              MainFxController mainFxController) {

        this.mainWindowViewController = mainWindowViewController;
        this.errorHandler = errorHandler;
        this.controllerListener = mainFxController;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        this.stage = stage;
        this.deck = deck;
        this.card = card;
        this.front = front;
        this.deckDAO = deckDAO;
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(String html) {
        try {
            Document doc = Jsoup.parse(html);
            Element body = doc.body();
            body.removeAttr("contenteditable");

            if (front)
                card.setFront(doc.html());
            else
                ((FlashCard) card).setBack(doc.html());

            deckDAO.saveDeck(deck);
            controllerListener.savedChanges();

        } catch (Exception e) {
            errorHandler.savingError(e);
        }
    }

    public void show() {
        if (front)
            editCardViewController.setContent(card.getFront());
        else
            editCardViewController.setContent(((FlashCard) card).getBack());

        mainWindowViewController.setEditCardViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        stage.show();
    }

    public interface ControllerListener {
        void savedChanges();
    }
}
