// service/common/ValidatorFactory.java
package service.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import service.customer.AddToCartRequestValidator;
public class ValidatorFactory {

    private static final Map<String, Validator<?>> validators = new ConcurrentHashMap<>();

    static {
        validators.put("customer", new CustomerValidator());
        validators.put("admin", new AdminValidator());
        validators.put("staff", new StaffValidator());
        validators.put("addToCart", new AddToCartRequestValidator());
        validators.put("delivery", new DeliveryPartnerValidator());


    }

   @SuppressWarnings("unchecked")
public static <T> Validator<T> getValidator(String userType) {
    Validator<?> validator = validators.get(userType);
    if (validator == null) {
        throw new IllegalArgumentException("No validator found for userType: " + userType);
    }
    return (Validator<T>) validator;
}


    public static void registerValidator(String userType, Validator<?> validator) {
        validators.put(userType, validator);
    }
}
