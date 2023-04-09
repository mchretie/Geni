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
    FontIcon deleteAnswerIcon;

    @FXML
    TextField answerTextField;

    private Listener listener;

    private int idxOfAnswer;

    private boolean isCorrectAnswer = false;


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

    public void setCorrectAnswer() {
        isCorrectAnswer = true;
        checkIcon.setIconColor(Color.web("#4ab831"));
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
        listener.setCorrectAnswer(idxOfAnswer);
    }

    @FXML
    private void handleDeleteAnswer(){
        listener.deleteAnswer(idxOfAnswer);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleCheckHoverExit() {
        if (!isCorrectAnswer) checkIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleCheckHover(){
        if (!isCorrectAnswer) checkIcon.setIconColor(Color.web("#7f8281"));
    }

    @FXML
    private void handleDeleteHoverExit() {
        deleteAnswerIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleDeleteHover(){
        deleteAnswerIcon.setIconColor(Color.web("#7f8281"));
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener{
        void setCorrectAnswer(int idxOfAnswer);
        void answerChanged(int idxOfAnswer, String answerText);

        void deleteAnswer(int idxOfAnswer);
    }
}
