package ulb.infof307.g01.gui.controller;

import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

public class EditCardController implements EditCardViewController.Listener {

    private final MainWindowViewController mainWindowViewController;
    private final EditCardViewController editCardViewController;
    private final ControllerListener controllerListener;

    public EditCardController(MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener) {
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.editCardViewController = mainWindowViewController.getEditCardViewController();
        editCardViewController.setListener(this);
    }

    @Override
    public void saveButtonClicked(String html) {
        System.out.println("Save button clicked");
        System.out.println(html);
    }

    @Override
    public void editCardClicked() {
        controllerListener.editCardClicked();
    }

    public interface ControllerListener {
        void editCardClicked();
    }
}
