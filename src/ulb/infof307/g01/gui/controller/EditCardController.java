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

    public EditCardController(MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener, Stage stage, Deck deck, DeckDAO deckDAO) {
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        this.stage = stage;
        this.deck = deck;
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
        } catch (Exception e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void editCardClicked() {
        controllerListener.editCardClicked(deck);
    }

    public interface ControllerListener {
        void editCardClicked(Deck deck);

        void savingError(Exception e);
    }
}
