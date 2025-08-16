package common;

import controller.ResendOtpServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.common.OtpSendService;
import service.common.UserService;

import util.contannts.AttributeKeys;
import util.contannts.ParameterKeys;
import util.contannts.PagePaths;
import mockhttp.*;
import util.OtpRateLimiter;
import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
import dto.UserIdTypePair;
import static org.junit.Assert.*;

public class ResendOtpServletTest {

    private ResendOtpServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockUserService mockUserService;
    private MockOtpSendService mockOtpSendService;
    private MockOtpRateLimiter mockOtpRateLimiter;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() throws Exception {
        // Redirect System.out and System.err to capture output
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        
        servlet = new ResendOtpServlet();
        // Manually call the init() method to ensure the servlet is fully initialized
        servlet.init();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        request.setMethod("POST");

        mockUserService = new MockUserService();
        mockOtpSendService = new MockOtpSendService();
        mockOtpRateLimiter = new MockOtpRateLimiter();

        // Use reflection to replace the fields with your mock objects
        java.lang.reflect.Field userServiceField = ResendOtpServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(servlet, mockUserService);

        java.lang.reflect.Field otpSendServiceField = ResendOtpServlet.class.getDeclaredField("otpSendService");
        otpSendServiceField.setAccessible(true);
        otpSendServiceField.set(servlet, mockOtpSendService);

        java.lang.reflect.Field otpRateLimiterField = ResendOtpServlet.class.getDeclaredField("otpRateLimiter");
        otpRateLimiterField.setAccessible(true);
        otpRateLimiterField.set(servlet, mockOtpRateLimiter);
    }
    
    @After
    public void restoreStreams() {
        // Restore the original System.out and System.err after each test
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // --- Invalid Request Tests ---

    @Test
    public void testInvalidRequest_missingUserId_shouldForwardWithError() throws ServletException, IOException {
        request.setParameter(ParameterKeys.USER_ID, "");
        request.setParameter(ParameterKeys.USER_TYPE, "customer");
        
        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("Invalid OTP request. Please provide all required fields.", request.getAttribute(AttributeKeys.ERROR));
    }

    @Test
    public void testInvalidRequest_invalidUserType_shouldForwardWithError() throws ServletException, IOException {
        request.setParameter(ParameterKeys.USER_ID, "CUS001");
        request.setParameter(ParameterKeys.USER_TYPE, "invalid_type");
        
        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        
        String errorMessage = (String) request.getAttribute(AttributeKeys.ERROR);
        assertNotNull(errorMessage);
        assertTrue(errorMessage.startsWith("Invalid OTP request."));
    }

    // --- Rate Limiting Tests ---

    @Test
    public void testRateLimited_shouldForwardWithRateLimitError() throws ServletException, IOException {
        String userId = "CUS001";
        String userType = "customer";
        
        request.setParameter(ParameterKeys.USER_ID, userId);
        request.setParameter(ParameterKeys.USER_TYPE, userType);
        
        mockOtpRateLimiter.setRateLimited(true);

        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        
        String errorMessage = (String) request.getAttribute(AttributeKeys.ERROR);
        assertNotNull(errorMessage);
        
        assertTrue(errorMessage.startsWith("Too many OTP requests."));
    }

    // --- Successful Resend Tests ---

    @Test
    public void testSuccessfulResend_shouldForwardWithSuccessMessage() throws ServletException, IOException {
        String userId = "CUS001";
        String userType = "customer";
        
        request.setParameter(ParameterKeys.USER_ID, userId);
        request.setParameter(ParameterKeys.USER_TYPE, userType);
        
        mockUserService.setEmailToReturn("test.user@example.com");

        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("OTP has been successfully sent to your email.", request.getAttribute(AttributeKeys.SUCCESS));
        
        assertTrue(mockOtpSendService.wasCalled());
    }

    // --- User Not Found Tests ---

    @Test
    public void testUserNotFound_shouldForwardWithError() throws ServletException, IOException {
        String userId = "NONEXISTENT";
        String userType = "customer";
        
        request.setParameter(ParameterKeys.USER_ID, userId);
        request.setParameter(ParameterKeys.USER_TYPE, userType);

        mockUserService.setEmailToReturn(null);

        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("User not found. Please check your details and try again.", request.getAttribute(AttributeKeys.ERROR));
    }
    
    // --- Exception Handling Tests ---

    @Test
    public void testServiceException_shouldForwardWithError() throws ServletException, IOException {
        String userId = "CUS001";
        String userType = "customer";
        
        request.setParameter(ParameterKeys.USER_ID, userId);
        request.setParameter(ParameterKeys.USER_TYPE, userType);
        
        mockUserService.throwExceptionOnGetEmail(true);

        servlet.service(request, response);

        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("An internal error occurred while processing your request. Please try again later.", request.getAttribute(AttributeKeys.ERROR));
    }

    private static class MockUserService implements UserService {
        private String emailToReturn;
        private boolean shouldThrowException;
        private String userIdToReturn;
        private String userTypeToReturn;

        public void setEmailToReturn(String email) {
            this.emailToReturn = email;
        }

        public void throwExceptionOnGetEmail(boolean throwException) {
            this.shouldThrowException = throwException;
        }

        public void setUserIdAndTypeToReturn(String userId, String userType) {
            this.userIdToReturn = userId;
            this.userTypeToReturn = userType;
        }

        @Override
        public String getEmailByUserIdAndType(String userId, String userType) throws Exception {
            if (shouldThrowException) {
                throw new Exception("Mock exception during email lookup.");
            }
            return emailToReturn;
        }

        @Override
        public Optional<UserIdTypePair> findUserIdAndTypeByEmail(String email) {
            if (userIdToReturn != null && userTypeToReturn != null) {
                return Optional.of(new UserIdTypePair(userIdToReturn, userTypeToReturn));
            }
            return Optional.empty();
        }
    }

    private static class MockOtpSendService implements OtpSendService {
        private boolean sendOtpCalled = false;
        
        @Override
        public void sendOtp(String userId, String userType, String email) throws Exception {
            this.sendOtpCalled = true;
        }
        
        public boolean wasCalled() {
            return sendOtpCalled;
        }
    }

    private static class MockOtpRateLimiter extends OtpRateLimiter {
        private boolean isRateLimited = false;
    
        public MockOtpRateLimiter() {
            super(1, 1); 
        }

        public void setRateLimited(boolean isRateLimited) {
            this.isRateLimited = isRateLimited;
        }

        @Override
        public boolean tryAcquire(String key) {
            return !isRateLimited;
        }

        @Override
        public long getRetryAfter(String key) {
            return 60000;
        }
    }
}