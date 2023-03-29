package ulb.infof307.g01.gui.view.profile;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.ResourceBundle;


public class ProfileViewController implements Initializable {


  @FXML
  private BorderPane rootPane;

  @FXML
  private VBox menuPane;

  private Listener listener;




  /* ====================================================================== */
  /*                              Initializer                               */
  /* ====================================================================== */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set the menu pane to be initially hidden
    menuPane.setTranslateX(200);

    // Set up the button to toggle the menu pane
    rootPane.setOnMouseClicked(event -> {
      if (event.getX() >= rootPane.getWidth() - 200) {
        // If the user clicks within the right 200 pixels of the window, toggle the menu pane
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), menuPane);
        if (menuPane.getTranslateX() != 0) {
          // If the menu pane is currently hidden, slide it in from the right
          transition.setToX(0);
        } else {
          // If the menu pane is currently visible, slide it out to the right
          transition.setToX(200);
        }
        transition.play();
      }
    });
  }

  /* ====================================================================== */
  /*                                Setters                                 */
  /* ====================================================================== */


  public void setListener(Listener listener) { this.listener = listener; }



  /* ====================================================================== */
  /*                             Click handlers                             */
  /* ====================================================================== */



  /* ====================================================================== */
  /*                           Listener Interface                           */
  /* ====================================================================== */

  public interface Listener {
    //
  }

}
