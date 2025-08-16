import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ServletContextMethodsPrinter {
    public static void main(String[] args) {
        Method[] methods = ServletContext.class.getMethods();

        System.out.println("Abstract methods in javax.servlet.ServletContext:");
        for (Method m : methods) {
            // Interface methods are implicitly abstract
            // Just print method signatures
            System.out.println(m.getReturnType().getSimpleName() + " " + m.getName() + "(" +
                java.util.Arrays.stream(m.getParameterTypes())
                    .map(Class::getSimpleName)
                    .reduce((a,b) -> a + ", " + b).orElse("") + ")");
        }
    }
}
