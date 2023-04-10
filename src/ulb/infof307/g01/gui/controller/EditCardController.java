package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

public class EditCardController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;
    private final ControllerListener controllerListener;
    private final Stage stage;
    private final DeckDAO deckDAO;
    private final Deck deck;
    private final Card card;

    public EditCardController(Stage stage, Deck deck, Card card, DeckDAO deckDAO, MainWindowViewController mainWindowViewController, MainFxController mainFxController) {
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = mainFxController;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        this.stage = stage;
        this.deck = deck;
        this.card = card;
        this.deckDAO = deckDAO;
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(Card card, String html) {
        try {
            Document doc = Jsoup.parse(html);
            Element body = doc.body();
            body.removeAttr("contenteditable");
            card.setFront(doc.html());
            deckDAO.saveDeck(deck);
            controllerListener.savedChanges();

        } catch (Exception e) {
            controllerListener.savingError(e);
        }
    }

    public void show() {
        editCardViewController.setCard(card);
        mainWindowViewController.setEditCardViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        stage.show();
    }

    public interface ControllerListener {
        void savingError(Exception e);

        void savedChanges();
    }
}
