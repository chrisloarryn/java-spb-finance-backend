package accounttransaction.repository;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import accounttransaction.entities.Account;
import accounttransaction.entities.enums.AccountType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class AccountRepositoryDataJpaTests {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM movements");
        jdbcTemplate.execute("DELETE FROM accounts");
    }

    @Test
    void saveAndLookupAccountByNumberAndPersonId() {
        UUID personId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Account account = new Account();
        account.setAccountNumber("ACC-001");
        account.setInitialBalance(1000.0);
        account.setStatus(true);
        account.setAccountType(AccountType.SAVINGS);
        account.setPersonId(personId);

        Account saved = repository.saveAndFlush(account);

        assertTrue(repository.findByAccountNumber(saved.getAccountNumber()).isPresent());
        assertEquals(1, repository.findByPersonId(personId).orElseThrow().size());
    }
}
