package ulb.infof307.g01.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main view of the application
 */
public class MainView implements View{

    Pane centerView;
    BorderPane view;
    ArrayList<Button> leftBarButtons;
    int currentTabIndex;

    public MainView() {
        view = new BorderPane();
        view.setBackground(Background.fill(Color.web("#635f63")));
        currentTabIndex = 0;

        VBox leftView = new VBox();
        leftView.setBackground(Background.fill(Color.web("#444444")));
        leftView.setPrefWidth(230);

        leftBarButtons = new ArrayList<Button>();

        String iconColor = "#AAAAAA";
        String[][] leftBarButtonsData = new String[][]{
                {"Welcome", "mdi2h-home"},
                {"Play deck", "mdi2p-play"},
                {"Edit deck", "mdi2p-pencil"},
                {"About us", "mdi2i-information"}};

        for (String[] buttonData : leftBarButtonsData) {
            Button button = new Button(buttonData[0]);
            FontIcon icon = new FontIcon(buttonData[1]);
            icon.setIconColor(Color.web(iconColor));
            button.setGraphic(icon);
            button.setPrefWidth(230);
            button.setPrefHeight(40);
            button.setStyle("-fx-background-radius: 0");
            leftBarButtons.add(button);
        }

        // set width and height
        for (Button button : leftBarButtons) {
            button.setPrefWidth(230);
            button.setPrefHeight(40);
            button.setStyle("-fx-background-radius: 0");
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #AAAAAA");
        }

        // set actions when button is pressed
        setTabsHandler();

        // highlight current tab (welcome tab at class construction)
        leftBarButtons.get(0).setStyle("-fx-background-color: #666666; -fx-text-fill: white");
        setCenterView(new WelcomeView());

        leftView.getChildren().addAll(leftBarButtons);
        view.setLeft(leftView);
    }

    private void setTabsHandler() {
        leftBarButtons.get(0).setOnAction(e -> {
            switchTab(0);
            setCenterView(new WelcomeView());
        });

        leftBarButtons.get(1).setOnAction(e -> {
            switchTab(1);
            setCenterView(new GameView());
        });

        leftBarButtons.get(2).setOnAction(e -> {
            switchTab(2);
            setCenterView(new ManageDecksView());
        });

        leftBarButtons.get(3).setOnAction(e -> {
            switchTab(3);
            setCenterView(new AboutView());
        });
    }

    private void switchTab(int tabIndex) {
        leftBarButtons.get(currentTabIndex).setStyle("-fx-background-color: transparent; -fx-text-fill: #AAAAAA");
        currentTabIndex = tabIndex;
        leftBarButtons.get(tabIndex).setStyle("-fx-background-color: #666666; -fx-text-fill: white");
    }

    public void setCenterView(View centerView) {
        this.centerView = centerView.getPane();
        this.view.setCenter(this.centerView);
    }

    public Pane getPane() {
        return view;
    }
}
