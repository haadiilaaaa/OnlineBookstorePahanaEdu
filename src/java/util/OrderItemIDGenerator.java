package util;

public class OrderItemIDGenerator implements IDGenerator<String> {
    @Override
    public String generate() {
        return "OI" + System.currentTimeMillis();
    }
}