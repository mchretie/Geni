package ulb.infof307.g01.gui.view.deckmenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ulb.infof307.g01.model.DeckMetadata;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class SearchPopUpViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private DeckViewController.Listener listener;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(DeckViewController.Listener listener) {
        this.listener = listener;
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
    }
}
