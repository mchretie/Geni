package ulb.infof307.g01.views;

/**
 * The main view handler. Call it with MainViewHandler.getInstance()
 */
public class MainViewHandler {
    private static MainViewHandler instance;
    private final MainView mainView = new MainView();

    private MainViewHandler() {
    }

    /**
     * Get the instance of the main view handler
     * @return the instance of the main view handler
     */
    public static MainViewHandler getInstance() {
        if (instance == null) {
            instance = new MainViewHandler();
        }
        return instance;
    }

    /**
     * Get the main view
     * @return the main view
     */
    public MainView getMainView() {
        return instance.mainView;
    }

    /**
     * Set the center view of the main view
     * @param view the view to set
     */
    public void setCenterView(View view) {
        instance.mainView.setCenterView(view);
    }
}
