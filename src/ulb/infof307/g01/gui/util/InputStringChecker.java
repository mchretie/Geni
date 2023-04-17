package ulb.infof307.g01.gui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputStringChecker {

    public static boolean isValidDeckName(String name) {
        name = name.replace(" ", "");
        return isCleanInput(name) && !name.isEmpty();
    }

    public static boolean isValidCredential(String name) {
        return isCleanInput(name);
    }

    private static boolean isCleanInput(String name) {
        Matcher matcher = Pattern.compile("[^A-Za-z0-9]")
                .matcher(name);

        return !matcher.find();
    }
}
