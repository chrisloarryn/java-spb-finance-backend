package personclient.repository;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import personclient.entities.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ClientRepositoryDataJpaTests {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM persons");
    }

    @Test
    void saveAndFindByIdPersistsClientsOnTheSingleTableInheritanceModel() {
        Client client = new Client();
        client.setName("Jane Doe");
        client.setClient_id("jane-001");
        client.setPassword("secret");
        client.setGender("F");
        client.setAge(30);
        client.setEmail_identifier("jane@example.com");
        client.setAddress("Street 123");
        client.setPhone_number("+56912345678");
        client.setStatus("ACTIVO");

        Client saved = repository.saveAndFlush(client);

        assertTrue(repository.findById(saved.getId()).isPresent());
        assertEquals(
                "Client",
                jdbcTemplate.queryForObject(
                        "SELECT dtype FROM persons WHERE id = ?",
                        String.class,
                        saved.getId()));
    }
}
