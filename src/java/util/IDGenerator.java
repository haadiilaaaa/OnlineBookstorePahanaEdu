package util;

public class IDGenerator {

    public static String generateId(String userTypePrefix, int currentCount) {
        int nextId = currentCount + 1;
        return String.format("%s__%02d", userTypePrefix, nextId);
    }
}
