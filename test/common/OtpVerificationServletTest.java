package common;

import controller.OtpVerificationServlet;
import org.junit.Before;
import org.junit.Test;
import service.common.OtpVerificationService;
import util.contannts.AttributeKeys;
import util.contannts.ErrorMessages;
import util.contannts.PagePaths;
import util.contannts.ParameterKeys;
import util.redirect.OtpRedirectStrategy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mockhttp.*; // Assuming these are your mock classes

import static org.junit.Assert.*;

public class OtpVerificationServletTest {

    private OtpVerificationServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private FakeOtpVerificationService fakeOtpService;
    private Map<String, OtpRedirectStrategy> mockRedirectStrategies;

    @Before
    public void setUp() throws ServletException {
        servlet = new OtpVerificationServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        servletContext = new MockServletContext();

        request.setSession(session);
        fakeOtpService = new FakeOtpVerificationService();
        servletContext.setAttribute("OtpVerificationService", fakeOtpService);

        mockRedirectStrategies = new HashMap<>();
        mockRedirectStrategies.put("customer", (req, resp) -> resp.sendRedirect(PagePaths.CUSTOMER_DASHBOARD_PAGE));
        mockRedirectStrategies.put("admin", (req, resp) -> resp.sendRedirect(PagePaths.ADMIN_DASHBOARD_PAGE));
        mockRedirectStrategies.put("delivery", (req, resp) -> resp.sendRedirect(PagePaths.DELIVERY_PARTNER_DASHBOARD_PAGE));

        servletContext.setAttribute("OtpRedirectStrategies", mockRedirectStrategies);

        ServletConfig config = new MockServletConfig(servletContext);
        servlet.init(config);
    }
    
    // --- Existing Tests ---
    
    @Test
    public void testValidOtp() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validUser");
        request.setParameter(ParameterKeys.USER_TYPE, "customer");
        request.setParameter(ParameterKeys.OTP_CODE, "1234");
        fakeOtpService.setOtpVerified(true, "customer"); // Set fake service to return true for customer

        request.setMethod("POST");
        servlet.service(request, response);

        assertEquals(PagePaths.CUSTOMER_DASHBOARD_PAGE, response.getRedirectedUrl());
        assertEquals("Verification successful! You can now login.", session.getAttribute("successMessage"));
    }

    @Test
    public void testInvalidOtp() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validUser");
        request.setParameter(ParameterKeys.USER_TYPE, "customer");
        request.setParameter(ParameterKeys.OTP_CODE, "9999");
        fakeOtpService.setOtpVerified(false, "customer"); // Set fake service to return false

        request.setMethod("POST");
        servlet.service(request, response);

        assertNull(response.getRedirectedUrl());
        assertEquals("Invalid or expired OTP.", request.getAttribute("error"));
    }

    @Test
    public void testMissingFields() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, ""); // Missing user ID
        request.setParameter(ParameterKeys.USER_TYPE, "customer");
        request.setParameter(ParameterKeys.OTP_CODE, "1234");
        
        request.setMethod("POST");
        servlet.service(request, response);

        assertNull(response.getRedirectedUrl());
        assertEquals("All fields are required.", request.getAttribute("error"));
    }
    
    // --- New Tests for Admin and Delivery Partner ---

    @Test
    public void testValidOtpForAdmin_shouldRedirectToAdminDashboard() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validAdmin");
        request.setParameter(ParameterKeys.USER_TYPE, "admin");
        request.setParameter(ParameterKeys.OTP_CODE, "1234");
        fakeOtpService.setOtpVerified(true, "admin");

        request.setMethod("POST");
        servlet.service(request, response);

        assertEquals(PagePaths.ADMIN_DASHBOARD_PAGE, response.getRedirectedUrl());
        assertEquals("Verification successful! You can now login.", session.getAttribute("successMessage"));
        assertNull(request.getAttribute(AttributeKeys.ERROR));
    }

    @Test
    public void testValidOtpForDeliveryPartner_shouldRedirectToDeliveryDashboard() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validDelivery");
        request.setParameter(ParameterKeys.USER_TYPE, "delivery");
        request.setParameter(ParameterKeys.OTP_CODE, "1234");
        fakeOtpService.setOtpVerified(true, "delivery");

        request.setMethod("POST");
        servlet.service(request, response);

        assertEquals(PagePaths.DELIVERY_PARTNER_DASHBOARD_PAGE, response.getRedirectedUrl());
        assertEquals("Verification successful! You can now login.", session.getAttribute("successMessage"));
        assertNull(request.getAttribute(AttributeKeys.ERROR));
    }

    @Test
    public void testInvalidOtpForAdmin_shouldForwardWithError() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validAdmin");
        request.setParameter(ParameterKeys.USER_TYPE, "admin");
        request.setParameter(ParameterKeys.OTP_CODE, "9999");
        fakeOtpService.setOtpVerified(false, "admin");

        request.setMethod("POST");
        servlet.service(request, response);

        assertNull(response.getRedirectedUrl());
        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("Invalid or expired OTP.", request.getAttribute(AttributeKeys.ERROR));
    }
    
    @Test
    public void testInvalidOtpForDeliveryPartner_shouldForwardWithError() throws Exception {
        request.setParameter(ParameterKeys.USER_ID, "validDelivery");
        request.setParameter(ParameterKeys.USER_TYPE, "delivery");
        request.setParameter(ParameterKeys.OTP_CODE, "9999");
        fakeOtpService.setOtpVerified(false, "delivery");

        request.setMethod("POST");
        servlet.service(request, response);

        assertNull(response.getRedirectedUrl());
        assertEquals(PagePaths.OTP_VERIFICATION_PAGE, response.getForwardedUrl());
        assertEquals("Invalid or expired OTP.", request.getAttribute(AttributeKeys.ERROR));
    }
    
    
    // --- Mock Service to handle multiple user types ---
    
    private static class FakeOtpVerificationService implements OtpVerificationService {
        private Map<String, Boolean> otpVerificationStatus = new HashMap<>();
    
        public void setOtpVerified(boolean verified, String userType) {
            otpVerificationStatus.put(userType, verified);
        }
    
        @Override
        public boolean verifyOtp(String userId, String userType, String otpCode) throws Exception {
            return otpVerificationStatus.getOrDefault(userType, false);
        }
    }
}