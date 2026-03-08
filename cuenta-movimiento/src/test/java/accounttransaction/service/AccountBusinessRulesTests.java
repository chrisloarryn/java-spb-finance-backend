package accounttransaction.service;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import accounttransaction.business.rules.AccountBusinessRules;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountBusinessRulesTests {

    @Mock
    private AccountRepository repository;

    @Test
    void checkIfTodoExistsAllowsPersistedAccounts() {
        UUID accountId = UUID.randomUUID();
        when(repository.existsById(accountId)).thenReturn(true);

        AccountBusinessRules rules = new AccountBusinessRules(repository);

        assertDoesNotThrow(() -> rules.checkIfTodoExists(accountId));
    }

    @Test
    void checkIfTodoExistsRejectsUnknownAccounts() {
        UUID accountId = UUID.randomUUID();
        when(repository.existsById(accountId)).thenReturn(false);

        AccountBusinessRules rules = new AccountBusinessRules(repository);

        assertThrows(AccountNotFoundException.class, () -> rules.checkIfTodoExists(accountId));
    }
}
