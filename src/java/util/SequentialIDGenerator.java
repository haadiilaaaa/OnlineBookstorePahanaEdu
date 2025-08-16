package util;

public class SequentialIDGenerator implements IDGenerator<String> {
    private final String prefix;
    private int nextNumber;

    public SequentialIDGenerator(String prefix, int startingNumber) {
        this.prefix = prefix;
        this.nextNumber = startingNumber;
    }

    @Override
    public String generate() {
        return String.format("%s%03d", prefix, nextNumber++);
    }
}