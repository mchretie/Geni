package ulb.infof307.g01.gui.view.deckpreview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ulb.infof307.g01.model.gamehistory.Game;

public class GameHistoryItemViewController {
    @FXML
    private Label dateLabel;
    @FXML
    private Label scoreLabel;

    public void setGame(Game game) {
        dateLabel.setText(game.getFormattedTimestamp());
        scoreLabel.setText(game.score());
    }
}
