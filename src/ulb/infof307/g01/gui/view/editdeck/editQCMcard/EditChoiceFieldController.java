package ulb.infof307.g01.gui.view.editdeck.editQCMcard;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditChoiceFieldController {
    @FXML
    TextField possibleAnswerText;

    @FXML
    FontIcon checkIcon;

    //TODO private Text possible answer

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

//    public void setListener(EditQCMCardViewController.Listener listener) {
//        this.listener = listener;
//    }

    /* ====================================================================== */
    /*                            Modified text                               */
    /* ====================================================================== */

    @FXML
    public void handleOnKeyPressed() {return; }

    /* ====================================================================== */
    /*                            Click handlers                              */
    /* ====================================================================== */

    @FXML
    private void handleCorrectAnswer(){return; }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleCheckHoverExit() {
        checkIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleCheckHover(){ checkIcon.setIconColor(Color.web("#7f8281"));  }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener{
        public void setPossibleAnswerText();
        public void setGoodAnswer();
    }
}
