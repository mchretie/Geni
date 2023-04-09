package ulb.infof307.g01.gui.view.editdeck.editQCMcard;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditAnswerFieldController {
    @FXML
    FontIcon checkIcon;

    @FXML
    TextField answerTextField;

    private Listener listener;

    private int idxOfAnswer;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setAnswerText(String answerText, int idxOfAnswer) {
        this.idxOfAnswer = idxOfAnswer;
        answerTextField.setText(answerText);
    }

    public void isCorrectAnswer(boolean isCorrectAnswer) {
        if (isCorrectAnswer) {
            checkIcon.setIconColor(Color.web("#00ff00"));
        } else {
            checkIcon.setIconColor(Color.web("#000000"));
        }
    }

    /* ====================================================================== */
    /*                            Modified text                               */
    /* ====================================================================== */

    @FXML
    public void handleKeyPressedOnTextField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            listener.answerChanged(idxOfAnswer, answerTextField.getText());

    }

    /* ====================================================================== */
    /*                            Click handlers                              */
    /* ====================================================================== */

    @FXML
    private void handleCorrectAnswer(){
        checkIcon.setIconColor(Color.web("#00ff00"));
        listener.setCorrectAnswer(idxOfAnswer);
    }


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
        void setCorrectAnswer(int idxOfAnswer);
        void answerChanged(int idxOfAnswer, String answerText);

    }
}
