package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EditDeckViewController {

    @FXML
    private VBox cardsContainer;

    private EditDeckListener listener;
    public void setListener(EditDeckListener listener){
        this.listener = listener;
    }

    @FXML
    public void onAddCardButton(){
        Rectangle cardContainer = new Rectangle(330,25,  Color.WHITE);
        cardContainer.setStroke(Color.BLACK);

        cardsContainer.getChildren().add(cardContainer);
        listener.onAddCardButton();
    }

    /*
    Allows EditDeck not to depend on a single controller
    (only depends on an interface that defines it itself)
     */
    public interface EditDeckListener{
        void onAddCardButton();
    }
}
