package ulb.infof307.g01.gui.controller.errorhandler;

import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;

public class ErrorHandler {

    private final MainWindowViewController mainWindowViewController;

    public ErrorHandler(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }

    /**
     * Used to communicate errors that raised exceptions and require the user
     *  to restart the application.
     */
    private void communicateError(Exception e, String messageToUser) {
        mainWindowViewController.alertError(e.toString(), messageToUser);
    }

    /**
     * For exceptions that indicate that the app cannot continue to
     * function properly
     */
    public void restartApplicationError(Exception e) {
        communicateError(e, "Veuillez redémarrer l'application.");
        Platform.exit();
    }

    /**
     * For when changes to components (Decks, cards, etc.) fail to be saved in
     * the db
     */
    private void databaseModificationError(Exception e) {
        String message = "Vos modifications n’ont pas été enregistrées, "
                + "veuillez réessayer. Si le problème persiste, "
                + "redémarrez l’application";

        communicateError(e, message);
    }

    public void failedDeckExportError(Exception e) {
        String message = "L'exportation de votre deck a échoué "
                + "veuillez réessayer. Si le problème persiste, "
                + "redémarrez l’application";

        communicateError(e, message);
    }

    public void failedDeckImportError(JsonSyntaxException e) {
        String message = "L'importation du deck a échoué, " +
                "veuillez vérifier que le fichier est bien un fichier " +
                ".json.";

        communicateError(e, message);
    }

    private void failedFetchError(Exception e) {
        String message = "Le téléchargement depuis le serveur a échoué";

        communicateError(e, message);
    }

    /**
     * Used to communicate user errors that do not require the application to
     *  be restarted.
     *
     * @param title Title of the error
     * @param messageToUser Message to display to the user
     */
    private void communicateError(String title, String messageToUser) {
        mainWindowViewController.alertError(title, messageToUser);
    }

    public void invalidDeckName(char c) {
        String title = "Nom de paquet invalide.";
        String description = "Le nom de paquet que vous avez entré est invalide. "
                + "Veuillez entrer un nom de paquet qui ne contient pas le "
                + "caractère " + c + ".";

        communicateError(title, description);
    }

    public void emptyPacketError() {
        String title = "Paquet vide.";
        String description = "Le paquet que vous avez ouvert est vide.";
        communicateError(title, description);
    }

    public void severConnectionError() {
        String title = "Erreur avec le serveur";
        String description = "Le paquet n’a pu être téléchargé.";
        communicateError(title, description);
    }

    public void failedLoading(IOException e) {
        restartApplicationError(e);
    }

    public void savingError(Exception e) {
        databaseModificationError(e);
    }
}
