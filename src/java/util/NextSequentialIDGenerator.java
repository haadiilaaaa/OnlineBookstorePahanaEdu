package util;

import java.util.List;

public class NextSequentialIDGenerator implements IDGenerator<String> {
    private final String prefix;
    private final List<String> existingIds;

    public NextSequentialIDGenerator(String prefix, List<String> existingIds) {
        this.prefix = prefix;
        this.existingIds = existingIds;
    }

    @Override
    public String generate() {
        if (existingIds.isEmpty()) {
            return String.format("%s%03d", prefix, 1);
        }

        for (int i = 1; i <= existingIds.size() + 1; i++) {
            String candidate = String.format("%s%03d", prefix, i);
            if (!existingIds.contains(candidate)) {
                return candidate;
            }
        }
        return String.format("%s%03d", prefix, existingIds.size() + 1);
    }
}