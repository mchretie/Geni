package ulb.infof307.g01.gui.controller;

import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.Card;

public class EditCardController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;
    private final ControllerListener controllerListener;

    private final DeckDAO deckDAO;

    public EditCardController(MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener, DeckDAO deckDAO) {
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        this.deckDAO = deckDAO;
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(Card card) {
        System.out.println("Save button clicked");
        System.out.println(card.getFront());
        try {
            deckDAO.updateCard(card);
        } catch (Exception e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void editCardClicked() {
        controllerListener.editCardClicked();
    }

    public interface ControllerListener {
        void editCardClicked();

        void savingError(Exception e);
    }
}
