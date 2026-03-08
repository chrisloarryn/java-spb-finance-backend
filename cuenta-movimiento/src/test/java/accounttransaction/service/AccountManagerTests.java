package accounttransaction.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import accounttransaction.business.concretes.AccountManager;
import accounttransaction.business.dto.requests.create.CreateAccountRequest;
import accounttransaction.business.dto.requests.update.UpdateAccountRequest;
import accounttransaction.business.dto.responses.create.CreateAccountResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllAccountsResponse;
import accounttransaction.business.dto.responses.update.UpdateAccountResponse;
import accounttransaction.business.rules.AccountBusinessRules;
import accounttransaction.entities.Account;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.entities.enums.AccountType;
import accounttransaction.exceptions.BadRequestException;
import accounttransaction.repository.AccountRepository;
import accounttransaction.utils.mappers.ModelMapperService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountManagerTests {

    @Mock
    private AccountRepository repository;

    @Mock
    private ModelMapperService mapper;

    @Mock
    private AccountBusinessRules rules;

    @Mock
    private ModelMapper requestMapper;

    @Mock
    private ModelMapper responseMapper;

    private AccountManager manager;

    @BeforeEach
    void setUp() {
        manager = new AccountManager(repository, mapper, rules);
        when(mapper.forRequest()).thenReturn(requestMapper);
        when(mapper.forResponse()).thenReturn(responseMapper);
    }

    @Test
    void getAllMapsRepositoryRows() {
        Account account = buildAccount();
        GetAllAccountsResponse response = new GetAllAccountsResponse();
        response.setId(account.getId());
        when(repository.findAll()).thenReturn(List.of(account));
        when(responseMapper.map(account, GetAllAccountsResponse.class)).thenReturn(response);

        List<GetAllAccountsResponse> result = manager.getAll();

        assertEquals(1, result.size());
        assertSame(response, result.getFirst());
    }

    @Test
    void getByIdReturnsTheMappedAccountWhenItExists() {
        UUID accountId = UUID.randomUUID();
        Account account = buildAccount();
        GetAccountResponse response = new GetAccountResponse();
        response.setId(accountId);
        when(repository.existsById(accountId)).thenReturn(true);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));
        when(responseMapper.map(account, GetAccountResponse.class)).thenReturn(response);

        assertSame(response, manager.getById(accountId));
    }

    @Test
    void getByIdRejectsMissingAccounts() {
        UUID accountId = UUID.randomUUID();
        when(repository.existsById(accountId)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> manager.getById(accountId));
    }

    @Test
    void getByAccountNumberReturnsTheMappedAccount() {
        Account account = buildAccount();
        GetAccountResponse response = new GetAccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        when(repository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(responseMapper.map(account, GetAccountResponse.class)).thenReturn(response);

        assertSame(response, manager.getByAccountNumber(account.getAccountNumber()));
    }

    @Test
    void addPersistsTheMappedAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        Account mappedAccount = buildAccount();
        Account persistedAccount = buildAccount();
        CreateAccountResponse response = new CreateAccountResponse();
        response.setId(persistedAccount.getId());
        when(requestMapper.map(request, Account.class)).thenReturn(mappedAccount);
        when(repository.save(mappedAccount)).thenReturn(persistedAccount);
        when(responseMapper.map(persistedAccount, CreateAccountResponse.class)).thenReturn(response);

        assertSame(response, manager.add(request));
        verify(repository).save(mappedAccount);
    }

    @Test
    void addRethrowsBadRequestExceptionsFromTheMapper() {
        CreateAccountRequest request = new CreateAccountRequest();
        when(requestMapper.map(request, Account.class)).thenThrow(new BadRequestException("invalid payload", "400"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> manager.add(request));

        assertEquals("invalid payload", exception.getMessage());
        assertEquals("400", exception.getCode());
    }

    @Test
    void updateRejectsMissingAccounts() {
        UUID accountId = UUID.randomUUID();
        when(repository.existsById(accountId)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> manager.update(accountId, new UpdateAccountRequest()));
    }

    @Test
    void updateSavesTheMappedAccountWithTheRequestedIdentifier() {
        UUID accountId = UUID.randomUUID();
        UpdateAccountRequest request = new UpdateAccountRequest();
        Account mappedAccount = buildAccount();
        UpdateAccountResponse response = new UpdateAccountResponse();
        response.setId(accountId);
        when(repository.existsById(accountId)).thenReturn(true);
        when(requestMapper.map(request, Account.class)).thenReturn(mappedAccount);
        when(responseMapper.map(mappedAccount, UpdateAccountResponse.class)).thenReturn(response);

        assertSame(response, manager.update(accountId, request));
        assertEquals(accountId, mappedAccount.getId());
        verify(repository).save(mappedAccount);
    }

    @Test
    void deleteRemovesExistingAccounts() {
        UUID accountId = UUID.randomUUID();
        when(repository.existsById(accountId)).thenReturn(true);

        manager.delete(accountId);

        verify(repository).deleteById(accountId);
    }

    @Test
    void hasEnoughBalanceReturnsTrueWhenTheAccountCanCoverTheWithdrawal() {
        Account account = buildAccount();
        account.setInitialBalance(500.0);
        when(repository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertTrue(manager.hasEnoughBalance(account.getAccountNumber(), -250.0));
    }

    @Test
    void hasEnoughBalanceReturnsFalseWhenTheAccountCannotCoverTheWithdrawal() {
        Account account = buildAccount();
        account.setInitialBalance(100.0);
        when(repository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertFalse(manager.hasEnoughBalance(account.getAccountNumber(), -250.0));
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setAccountNumber("ACC-001");
        account.setInitialBalance(1000.0);
        account.setStatus(true);
        account.setAccountType(AccountType.Ahorro);
        account.setPersonId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        return account;
    }
}
