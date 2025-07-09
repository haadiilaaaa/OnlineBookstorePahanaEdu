package command.admin.category;

import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;

public interface CategoryActionCommand {
   CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
