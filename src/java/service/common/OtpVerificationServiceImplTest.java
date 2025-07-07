import service.common.*;
import dao.*;
import model.OtpToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OtpVerificationServiceImplTest {

    private OtpVerificationService service;
    private OtpTokenDAO otpTokenDAO;
    private UserVerificationStrategyContext strategyContext;

    @BeforeEach
    public void setUp() {
        otpTokenDAO = mock(OtpTokenDAO.class);
        strategyContext = mock(UserVerificationStrategyContext.class);

        service = new OtpVerificationServiceImpl(otpTokenDAO, strategyContext);
    }

    @Test
    public void testInvalidOtpReturnsFalse() throws Exception {
        when(otpTokenDAO.findValidToken("cus__01", "customer", "123456")).thenReturn(null);

        boolean result = service.verifyOtp("cus__01", "customer", "123456");

        assertFalse(result);
        verify(strategyContext, never()).verify(anyString(), anyString());
    }

    @Test
    public void testExpiredOtpReturnsFalse() throws Exception {
        OtpToken expiredToken = new OtpToken();
        expiredToken.setUsed(false);
        expiredToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(otpTokenDAO.findValidToken("cus__01", "customer", "123456")).thenReturn(expiredToken);

        boolean result = service.verifyOtp("cus__01", "customer", "123456");

        assertFalse(result);
        verify(strategyContext, never()).verify(anyString(), anyString());
    }

    @Test
    public void testValidOtpCallsStrategy() throws Exception {
        OtpToken validToken = new OtpToken();
        validToken.setId("token123");
        validToken.setUsed(false);
        validToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        when(otpTokenDAO.findValidToken("cus__01", "customer", "999999")).thenReturn(validToken);

        boolean result = service.verifyOtp("cus__01", "customer", "999999");

        assertTrue(result);
        verify(otpTokenDAO).markUsed("token123");
        verify(strategyContext).verify("customer", "cus__01");
    }
}
