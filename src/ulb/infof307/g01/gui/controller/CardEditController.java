package ulb.infof307.g01.gui.controller;

import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

public class CardEditController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;

    public CardEditController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(String html) {
        System.out.println("Save button clicked");
        System.out.println(html);
    }
}
