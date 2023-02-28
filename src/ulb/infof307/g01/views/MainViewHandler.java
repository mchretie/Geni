package ulb.infof307.g01.views;


public class MainViewHandler {
    private static MainViewHandler instance;
    private final MainView mainView = new MainView();

    private MainViewHandler() {
    }

    public static MainViewHandler getInstance() {
        if (instance == null) {
            instance = new MainViewHandler();
        }
        return instance;
    }

    public MainView getMainView() {
        return instance.mainView;
    }

    public void setCenterView(View view) {
        instance.mainView.setCenterView(view);
    }
}
