package util;

public class LoggerUtil {

    public static void logInfo(Class<?> clazz, String message) {
        System.out.println("[INFO] [" + clazz.getSimpleName() + "] " + message);
    }

    public static void logWarning(Class<?> clazz, String message) {
        System.out.println("[WARNING] [" + clazz.getSimpleName() + "] " + message);
    }

    public static void logError(Class<?> clazz, String message, Exception e) {
        System.out.println("[ERROR] [" + clazz.getSimpleName() + "] " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}
