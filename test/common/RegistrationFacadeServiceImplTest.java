package common;

import org.junit.Before;
import org.junit.Test;
import util.RegistrationRequestBuilder;
import util.UserIdExtractor;
import service.common.*;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

public class RegistrationFacadeServiceImplTest {

    private RegistrationFacadeServiceImpl service;
    private FakeCustomerService fakeCustomerService;

    @Before
    public void setUp() {
        fakeCustomerService = new FakeCustomerService();

        service = new RegistrationFacadeServiceImpl(
                fakeCustomerService,
                null, // admin service not tested here
                null, // staff service not tested here
                null  // delivery service not tested here
        );

        // Inject fake validator for "customer"
       ValidatorFactory.registerValidator("customer", dto -> {
    if ("fail".equals(((dto.CustomerDTO) dto).getFirstName())) {
        throw new RuntimeException("Validation failed");
    }
});


        // Inject fake converter for "customer"
        RegistrationRequestBuilder.registerConverter("customer", req -> new FakeDTO("ok"));

        // Inject fake userId extractor
        UserIdExtractor.registerExtractor("customer", (userType, dto) -> "USER123");
    }

    @Test
    public void testRegister_success() throws Exception {
        String id = service.register("customer", new FakeRequest());
        assertEquals("USER123", id);
        assertTrue(fakeCustomerService.wasCalled);
    }

    @Test(expected = RuntimeException.class)
    public void testRegister_validationFails() throws Exception {
        RegistrationRequestBuilder.registerConverter("customer", req -> new FakeDTO("fail"));
        service.register("customer", new FakeRequest());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegister_invalidUserType() throws Exception {
        service.register("unknown", new FakeRequest());
    }

    // -------- Fakes --------

   private static class FakeCustomerService implements service.customer.RegisterCustomerService {
    boolean wasCalled = false;

    @Override
    public void register(dto.CustomerDTO dto) {
        wasCalled = true;
    }
}

   private static class FakeDTO extends dto.CustomerDTO {
    FakeDTO(String firstName) {
        this.setFirstName(firstName);
    }
}



   private static class FakeRequest extends mockhttp.MockHttpServletRequest {
    // You can add any custom behavior here if needed
}

}
