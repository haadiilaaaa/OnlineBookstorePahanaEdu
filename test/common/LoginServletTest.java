package common;

import controller.LoginServlet;
import dto.UserSession;
import org.junit.Before;
import org.junit.Test;
import service.common.LoginService;
import util.LoginAttemptService;
import util.ValidationException;
import util.redirect.LoginRedirectStrategy;
import util.redirect.RedirectStrategyRegistry;
import mockhttp.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static util.contannts.AttributeKeys.ERROR;
import static util.contannts.ErrorMessages.*;
import static util.contannts.PagePaths.LOGIN_PAGE;
import static util.contannts.ParameterKeys.PASSWORD;
import static util.contannts.ParameterKeys.USERNAME;
import static util.contannts.SessionKeys.USER;

public class LoginServletTest {

    private LoginServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private FakeLoginService fakeLoginService;
    private MockLoginAttemptService mockAttemptService;
    private MockRedirectStrategy mockRedirectStrategy;

    @Before
    public void setUp() throws Exception {
        servlet = new LoginServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        
        request.setMethod("POST");
        request.setSession(session);

        fakeLoginService = new FakeLoginService();
        mockAttemptService = new MockLoginAttemptService();

        // Using reflection to set private fields, as you preferred
        java.lang.reflect.Field loginServiceField = LoginServlet.class.getDeclaredField("loginService");
        loginServiceField.setAccessible(true);
        loginServiceField.set(servlet, fakeLoginService);

        java.lang.reflect.Field attemptServiceField = LoginServlet.class.getDeclaredField("attemptService");
        attemptServiceField.setAccessible(true);
        attemptServiceField.set(servlet, mockAttemptService);
        
        // Mock RedirectStrategyRegistry setup
        RedirectStrategyRegistry.clearStrategies();
    RedirectStrategyRegistry.register("customer", new MockRedirectStrategy("customer"));
    RedirectStrategyRegistry.register("admin", new MockRedirectStrategy("admin"));
    RedirectStrategyRegistry.register("delivery_partner", new MockRedirectStrategy("delivery_partner"));
    }
    
    // --- Valid Login Tests ---
   @Test
public void testValidLogin_customer_shouldRedirectToDashboard() throws ServletException, IOException {
    // 1. Set up initial request parameters
    request.setParameter(USERNAME, "customerUser");
    request.setParameter(PASSWORD, "password");

    // 2. Call the servlet's service method
    servlet.service(request, response);

    // 3. Assert the redirect URL
    assertEquals("/dashboard/customer", response.getRedirectedUrl());

    // 4. Retrieve the *new* session from the request
    HttpSession newSession = request.getSession(false); 
    
    // 5. Assert the new session and its attributes
    assertNotNull(newSession);
    UserSession userSession = (UserSession) newSession.getAttribute(USER);
    assertNotNull(userSession);
    assertEquals("customer", userSession.getUserType());
    
}
    @Test
public void testValidLogin_admin_shouldRedirectToDashboard() throws ServletException, IOException {
    request.setParameter(USERNAME, "adminUser");
    request.setParameter(PASSWORD, "password");
    servlet.service(request, response);
    assertEquals("/dashboard/admin", response.getRedirectedUrl());
    
    // CORRECTION: Get the new session from the request, not the old session field
    HttpSession newSession = request.getSession(false);
    
    // Use the newSession object for assertions
    assertNotNull(newSession);
    UserSession userSession = (UserSession) newSession.getAttribute(USER);
    assertNotNull(userSession);
    assertEquals("admin", userSession.getUserType());
}

   @Test
public void testValidLogin_deliveryPartner_shouldRedirectToDashboard() throws ServletException, IOException {
    request.setParameter(USERNAME, "deliveryUser");
    request.setParameter(PASSWORD, "password");
    servlet.service(request, response);

    // 1. Assert the redirect URL first
    assertEquals("/dashboard/delivery_partner", response.getRedirectedUrl());

    // 2. Get the new session from the request
    HttpSession newSession = request.getSession(false);
    assertNotNull(newSession);

    // 3. Use the new session to get the user attribute
    UserSession userSession = (UserSession) newSession.getAttribute(USER);
    assertNotNull(userSession);
    assertEquals("delivery_partner", userSession.getUserType());
}
    // --- Invalid Login Tests ---
    @Test
    public void testInvalidLogin_wrongPassword_shouldForwardToLoginPage() throws ServletException, IOException {
        request.setParameter(USERNAME, "customerUser");
        request.setParameter(PASSWORD, "wrong_password");
        servlet.service(request, response);
        assertEquals(LOGIN_PAGE, response.getForwardedUrl());
        assertEquals(INVALID_CREDENTIALS, request.getAttribute(ERROR));
        assertNull(session.getAttribute(USER));
    }

    @Test
public void testInvalidLogin_emptyCredentials_shouldForwardToLoginPage() throws ServletException, IOException {
    request.setParameter(USERNAME, "");
    request.setParameter(PASSWORD, "");
    servlet.service(request, response);
    assertEquals(LOGIN_PAGE, response.getForwardedUrl());
    
    // Correct assertion based on the provided LoginRequestValidator code
    assertEquals("Username is required.", request.getAttribute(ERROR));
}

    // --- Login Attempt and Blocking Tests ---
 // Corrected testTooManyAttempts_shouldForwardWithBlockMessage
@Test
public void testTooManyAttempts_shouldForwardWithBlockMessage() throws ServletException, IOException {
    // Use the new mock method to block the specific user
    mockAttemptService.setBlocked("customerUser", true);

    request.setParameter(USERNAME, "customerUser");
    request.setParameter(PASSWORD, "password");
    servlet.service(request, response);
    
    assertEquals(LOGIN_PAGE, response.getForwardedUrl());
    
    String errorMessage = (String) request.getAttribute(ERROR);
    
    // Add an assertion to ensure the error message is not null
    assertNotNull(errorMessage);
    
    // The servlet builds the message dynamically, so we check if it starts with the correct string
   assertTrue(errorMessage.startsWith("Too many failed attempts."));
}

// Corrected testInvalidLogin_emptyCredentials_shouldForwardToLoginPage

    @Test
    public void testSuccessfulLoginAfterFailures_shouldResetAttempts() throws ServletException, IOException {
        mockAttemptService.recordFailure("customerUser");
        request.setParameter(USERNAME, "customerUser");
        request.setParameter(PASSWORD, "password");
        servlet.service(request, response);
        assertFalse(mockAttemptService.isBlocked("customerUser"));
        assertEquals("/dashboard/customer", response.getRedirectedUrl());
    }
    
    // --- Mock Classes ---
    private static class FakeLoginService implements LoginService {
        private final Map<String, UserSession> validUsers = new HashMap<>();
        public FakeLoginService() {
            // Your UserSession constructor now accepts 8 parameters
            validUsers.put("customerUser", new UserSession("CUS001", "customerUser", "password", "customer", "John", "Doe", "john.doe@example.com", "0771234567"));
            validUsers.put("adminUser", new UserSession("ADM001", "adminUser", "password", "admin", "Admin", "User", "admin@example.com", "0771234568"));
            validUsers.put("deliveryUser", new UserSession("DP001", "deliveryUser", "password", "delivery_partner", "Delivery", "Partner", "delivery@example.com", "0771234569"));
        }
        @Override
        public UserSession authenticate(String username, String password) throws ValidationException {
            if (validUsers.containsKey(username) && "password".equals(password)) {
                return validUsers.get(username);
            }
            return null;
        }
    }

  // Corrected MockLoginAttemptService
// Corrected MockLoginAttemptService
// Corrected MockLoginAttemptService to match real service's configuration
private static class MockLoginAttemptService extends LoginAttemptService {
    private Map<String, Boolean> blockedUsers = new HashMap<>();

    public MockLoginAttemptService() {
        // Match the configuration from LoginServlet's init method
        super(5, TimeUnit.MINUTES.toMillis(15));
    }

    @Override
    public boolean isBlocked(String username) {
        return blockedUsers.getOrDefault(username, false);
    }

    public void setBlocked(String username, boolean blocked) {
        blockedUsers.put(username, blocked);
    }

    @Override
    public void recordFailure(String username) { /* Mock implementation */ }

    @Override
    public void resetAttempts(String username) {
        blockedUsers.remove(username);
    }

    @Override
    public long getRemainingBlockTime(String username) {
        // Return a hardcoded time for the test
        return TimeUnit.MINUTES.toMillis(10); // Example, can be any value
    }
}
    // Corrected MockRedirectStrategy
// This is the correct version of the mock class to use in your test file
private static class MockRedirectStrategy implements LoginRedirectStrategy {
    private String userType;

    public MockRedirectStrategy(String userType) {
        this.userType = userType;
    }

    // MockRedirectStrategy
@Override
public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // This line is the problem
    // It's trying to get an attribute from a session that has been invalidated
    String role = (String) req.getSession().getAttribute("userRole"); 
    resp.sendRedirect("/dashboard/" + role);
}
}
}