package personclient.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import personclient.ClientPersonApiApplication;

@SpringBootTest(classes = ClientPersonApiApplication.class)
@ActiveProfiles("test")
class ClientPersonApiApplicationIntegrationTests {

    @Test
    void contextLoads() {
    }
}
