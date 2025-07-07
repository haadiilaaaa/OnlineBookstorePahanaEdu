package util;
import java.util.UUID;

public class IDGenerator {

    public static String generateId(String prefix, int number) {
        return String.format("%s%03d", prefix, number); // dis001, dis002, etc.
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    public static String generateOrderId(int nextNumber) {
    return generateId("ORD", nextNumber); // e.g., ORD001
}
// ✅ Add this for OrderItem IDs
    public static String generateOrderItemId() {
        return "OI" + System.currentTimeMillis(); // e.g., OI1720282315294
    }
    
    
}
