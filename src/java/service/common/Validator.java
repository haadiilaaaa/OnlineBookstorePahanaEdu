// service/common/Validator.java
package service.common;

import util.ValidationException;

public interface Validator<T> {
    void validate(T dto) throws ValidationException;
}
