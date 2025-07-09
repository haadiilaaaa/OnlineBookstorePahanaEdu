package util;  // or use: package exception;

public class DAOExeption extends Exception {
    public DAOExeption(String message) {
        super(message);
    }

    public DAOExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
