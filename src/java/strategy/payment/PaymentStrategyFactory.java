package strategy.payment;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequestListener;
import dao.PaymentDAO;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;


public class PaymentStrategyFactory {
    public static PaymentStrategy getStrategy(String method, HttpServletRequest req, PaymentDAO paymentDAO) {
        switch (method) {
            case "Credit Card":
                return new CreditCardPayment(
                    req.getParameter("cardNumber"),
                    req.getParameter("expiryDate"),
                    req.getParameter("cvv"),
                    paymentDAO
                );
            case "Debit Card":
                return new DebitCardPayment(
                    req.getParameter("cardNumber"),
                    req.getParameter("expiryDate"),
                    req.getParameter("cvv"),
                    paymentDAO
                );
            default:
                return new CashOnDeliveryPayment(paymentDAO);
        }
    }
}
