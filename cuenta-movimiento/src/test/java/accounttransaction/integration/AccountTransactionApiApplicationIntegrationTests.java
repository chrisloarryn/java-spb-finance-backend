package accounttransaction.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import accounttransaction.AccountTransactionApiApplication;

@SpringBootTest(classes = AccountTransactionApiApplication.class)
@ActiveProfiles("test")
class AccountTransactionApiApplicationIntegrationTests {

    @Test
    void contextLoads() {
    }
}
