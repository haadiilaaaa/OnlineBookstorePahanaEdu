package service.admin;

import java.util.HashMap;
import java.util.Map;

public class AddItemServiceManager {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> void register(Class<T> iface, T impl) {
        services.put(iface, impl);
    }

    public static <T> T get(Class<T> iface) {
        return iface.cast(services.get(iface));
    }
}
