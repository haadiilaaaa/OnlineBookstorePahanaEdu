package util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageResolver {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);

    // ✅ For simple key-only messages
    public static String get(String key) {
        return bundle.getString(key);
    }

    // ✅ For messages with placeholders/args
    public static String get(String key, Object... args) {
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, args);
    }
}
