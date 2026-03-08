package personclient.service;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import personclient.business.rules.ClientBusinessRules;
import personclient.entities.ClientNotFoundException;
import personclient.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientBusinessRulesTests {

    @Mock
    private ClientRepository repository;

    @Test
    void checkIfTodoExistsAllowsPersistedClients() {
        UUID clientId = UUID.randomUUID();
        when(repository.existsById(clientId)).thenReturn(true);

        ClientBusinessRules rules = new ClientBusinessRules(repository);

        assertDoesNotThrow(() -> rules.checkIfTodoExists(clientId));
    }

    @Test
    void checkIfTodoExistsRejectsUnknownClients() {
        UUID clientId = UUID.randomUUID();
        when(repository.existsById(clientId)).thenReturn(false);

        ClientBusinessRules rules = new ClientBusinessRules(repository);

        assertThrows(ClientNotFoundException.class, () -> rules.checkIfTodoExists(clientId));
    }
}
