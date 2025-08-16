package common;

import mockhttp.*;
import controller.RegisterServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import service.common.RegistrationFacadeService;
import util.ValidationException;
import util.IDGenerator;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.*;
import util.*;
import java.sql.*;

import static org.junit.Assert.*;

public class RegisterServletTest {

    private RegisterServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private DBRegistrationFacadeService realService;
    private FakeRegistrationFacadeService fakeService;

    @Before
    public void setUp() throws ServletException {
        servlet = new RegisterServlet();

        request = new MockHttpServletRequest() {
            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                return new MockRequestDispatcher(path);
            }
        };
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        servletContext = new MockServletContext();

        realService = new DBRegistrationFacadeService();

        request.setSession(session);
        servletContext.setAttribute("RegistrationFacadeService", realService);

        MockServletConfig config = new MockServletConfig(servletContext);
        servlet.init(config);
    }
    
    @After
    public void tearDown() {
        String url = "jdbc:mysql://localhost:3306/pahana_edu_v2";
        String dbUser = "root";
        String dbPass = "";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            // Delete all test users to prevent unique constraint violations
            String sql = "DELETE FROM customer WHERE username LIKE 'testuser_%'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
            sql = "DELETE FROM admin WHERE username LIKE 'testuser_%'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
            sql = "DELETE FROM delivery_partners WHERE username LIKE 'testuser_%'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoPost_insertCustomer_shouldSaveToDatabase() throws Exception {
        request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "customer");

        servlet.service(request, response);

        String redirectUrl = response.getRedirectedUrl();
        assertNotNull("Redirect expected after successful save", redirectUrl);
        System.out.println("✅ Successfully saved to database. Redirected to: " + redirectUrl);
    }

    @Test
    public void testDoPost_invalidUserType_shouldForwardToIndexPage() throws Exception {
        request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "invalid_type");

        servlet.service(request, response);

        assertEquals("index.jsp", response.getForwardedUrl());
        assertEquals("Unknown userType",
                request.getAttribute(util.contannts.AttributeKeys.ERROR));
    }

   @Test
public void testDoPost_existingCustomer_shouldShowAlreadyRegisteredMessage() throws Exception {
    // Override the default service set in setUp() with the fake one
    FakeRegistrationFacadeService fakeService = new FakeRegistrationFacadeService();
    servletContext.setAttribute("RegistrationFacadeService", fakeService);
    fakeService.setThrowDuplicateEmailException(true);

    // Make sure the servlet instance is using the new context attribute
    servlet.init(new MockServletConfig(servletContext)); 

    request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "customer");
    servlet.service(request, response);

    assertEquals(util.ErrorPageResolver.resolve("customer"), response.getForwardedUrl());
    assertEquals("Email already registered", request.getAttribute(util.contannts.AttributeKeys.ERROR));
}
    
        @Test
    public void testDoPost_insertAdmin_shouldSaveToDatabase() throws Exception {
        request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "admin");

        servlet.service(request, response);

        String redirectUrl = response.getRedirectedUrl();
        assertNotNull("Redirect expected after successful save", redirectUrl);
        System.out.println("✅ Successfully saved admin to database. Redirected to: " + redirectUrl);
    }

    @Test
public void testDoPost_existingAdmin_shouldShowAlreadyRegisteredMessage() throws Exception {
    // Override the default service set in setUp() with the fake one
    fakeService = new FakeRegistrationFacadeService();
    servletContext.setAttribute("RegistrationFacadeService", fakeService);
    fakeService.setThrowDuplicateEmailException(true);

    // Re-initialize the servlet instance to pick up the new service from the context
    servlet.init(new MockServletConfig(servletContext)); 

    request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "admin");
    servlet.service(request, response);

    assertEquals(util.ErrorPageResolver.resolve("admin"), response.getForwardedUrl());
    assertEquals("Email already registered",
                 request.getAttribute(util.contannts.AttributeKeys.ERROR));
}

    @Test
public void testDoPost_insertDeliveryPartner_shouldSaveToDatabase() throws Exception {
    request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "delivery_partner");
    System.out.println("User type for test: " + request.getParameter(util.contannts.ParameterKeys.USER_TYPE));
    servlet.service(request, response);

        String redirectUrl = response.getRedirectedUrl();
        assertNotNull("Redirect expected after successful save", redirectUrl);
        System.out.println("✅ Successfully saved delivery partner to database. Redirected to: " + redirectUrl);
    }

    @Test
public void testDoPost_existingDeliveryPartner_shouldShowAlreadyRegisteredMessage() throws Exception {
    fakeService = new FakeRegistrationFacadeService();
    servletContext.setAttribute("RegistrationFacadeService", fakeService);
    fakeService.setThrowDuplicateEmailException(true);

    // Re-initialize the servlet instance to pick up the new service from the context
    servlet.init(new MockServletConfig(servletContext)); //  ✅ This is crucial

    request.setParameter(util.contannts.ParameterKeys.USER_TYPE, "delivery_partner");
    servlet.service(request, response);

    assertEquals(util.ErrorPageResolver.resolve("delivery_partner"), response.getForwardedUrl());
    assertEquals("Email already registered",
            request.getAttribute(util.contannts.AttributeKeys.ERROR));
}
    /**
     * ✅ Real DB-backed registration service with unique IDs
     */
  private static class DBRegistrationFacadeService implements RegistrationFacadeService {
    // Helper method to fetch all existing IDs for a given user type
    private List<String> getExistingIds(String userType, Connection conn) throws SQLException {
        List<String> existingIds = new ArrayList<>();
        String tableName = switch (userType) {
            case "customer" -> "customer";
            case "admin" -> "admin";
            case "delivery_partner" -> "delivery_partners";
            default -> throw new IllegalArgumentException("Unknown userType");
        };

        String sql = "SELECT id FROM " + tableName + " ORDER BY id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                existingIds.add(rs.getString("id"));
            }
        }
        return existingIds;
    }

    @Override
    public String register(String userType, HttpServletRequest req) throws Exception {
        String url = "jdbc:mysql://localhost:3306/pahana_edu_v2";
        String dbUser = "root";
        String dbPass = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String prefix = switch (userType) {
                case "customer" -> "CUS";
                case "admin" -> "ADM";
                case "delivery_partner" -> "DP";
                default -> throw new ValidationException("Unknown userType");
            };

            List<String> existingIds = getExistingIds(userType, conn);

            // Use the concrete NextSequentialIDGenerator class to generate the ID
            IDGenerator<String> idGenerator = new NextSequentialIDGenerator(prefix, existingIds);
            String id = idGenerator.generate();

            String sql = switch (userType) {
                case "customer" ->
                        "INSERT INTO customer (id, username, first_name, last_name, email, contact_number, address, password_hash, is_verified) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                case "admin" ->
                        "INSERT INTO admin (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                case "delivery_partner" ->
                        "INSERT INTO delivery_partners (id, username, first_name, last_name, email, contact_number, vehicle_number, status, is_verified) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                default -> throw new ValidationException("Unknown userType");
            };

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.setString(2, "testuser_" + userType);
                stmt.setString(3, "Test" + userType.substring(0, 1).toUpperCase());
                stmt.setString(4, "Account");
                stmt.setString(5, "test_" + userType + "@example.com");
                stmt.setString(6, "0771234567");

                if (userType.equals("customer")) {
                    stmt.setString(7, "99 Test Lane, Test City");
                    stmt.setString(8, "hashed_test_password");
                    stmt.setInt(9, 1);
                } else if (userType.equals("admin")) {
                    stmt.setString(7, "hashed_test_password");
                    stmt.setInt(8, 1);
                } else if (userType.equals("delivery_partner")) {
                    stmt.setString(7, "TEST-VEHICLE-999");
                    stmt.setString(8, "PENDING"); // Change to a valid ENUM value
                    stmt.setInt(9, 1);
                }

                stmt.executeUpdate();
                System.out.println("✅ Inserted " + userType + " with ID: " + id);
                return id;
            }
        }
    }
}
    /**
     * ✅ Fake registration service for unit tests
     */
    private static class FakeRegistrationFacadeService implements RegistrationFacadeService {
        private boolean throwDuplicateEmailException = false;

        public void setThrowDuplicateEmailException(boolean value) {
            this.throwDuplicateEmailException = value;
        }

        @Override
        public String register(String userType, HttpServletRequest request) throws Exception {
            if (throwDuplicateEmailException) {
                throw new ValidationException("Email already registered");
            }
            return "USER123";
        }
    }
    
    

    
}
