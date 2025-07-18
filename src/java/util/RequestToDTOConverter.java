package util;

import javax.servlet.http.HttpServletRequest;

public interface RequestToDTOConverter {
    Object convert(HttpServletRequest request);
}
