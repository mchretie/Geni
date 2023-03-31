package ulb.infof307.g01.gui.controller;

import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

public class EditCardController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;
    private final ControllerListener controllerListener;

    private final DeckDAO deckDAO;

    private Deck deck;

    public EditCardController(MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener, Deck deck, DeckDAO deckDAO) {
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        this.deck = deck;
        this.deckDAO = deckDAO;
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(Card card, String html) {
        try {
            card.setFront(html);
            System.out.println(deck.getCards().get(0).getFront());
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
