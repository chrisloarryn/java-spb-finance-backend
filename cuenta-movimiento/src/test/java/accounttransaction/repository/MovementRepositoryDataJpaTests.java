package accounttransaction.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import accounttransaction.entities.Movement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class MovementRepositoryDataJpaTests {

    @Autowired
    private MovementRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM movements");
        jdbcTemplate.execute("DELETE FROM accounts");
    }

    @Test
    void saveAndLookupMovementsByAccountNumber() {
        Movement movement = new Movement();
        movement.setAccountNumber("ACC-001");
        movement.setTransactionType("Ahorro");
        movement.setInitialBalance(1000.0);
        movement.setTransactionValue(100.0);
        movement.setStatus(true);
        movement.setDetail("Deposito de 100.0");

        repository.saveAndFlush(movement);

        assertTrue(repository.findByAccountNumber("ACC-001").isPresent());
        assertEquals(1, repository.findByAccountNumber("ACC-001").orElseThrow().size());
    }
}
