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
import org.springframework.http.HttpStatus;

import accounttransaction.business.concretes.MovementManager;
import accounttransaction.business.dto.requests.create.CreateMovementRequest;
import accounttransaction.business.dto.requests.update.UpdateMovementRequest;
import accounttransaction.business.dto.responses.create.CreateMovementResponse;
import accounttransaction.business.dto.responses.get.GetAllMovementsResponse;
import accounttransaction.business.dto.responses.get.GetMovementResponse;
import accounttransaction.business.dto.responses.update.UpdateMovementResponse;
import accounttransaction.business.rules.AccountBusinessRules;
import accounttransaction.entities.Account;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.entities.Movement;
import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.OperationType;
import accounttransaction.exceptions.BadRequestException;
import accounttransaction.repository.AccountRepository;
import accounttransaction.repository.MovementRepository;
import accounttransaction.utils.mappers.ModelMapperService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovementManagerTests {

    @Mock
    private MovementRepository repository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapperService mapper;

    @Mock
    private AccountBusinessRules rules;

    @Mock
    private ModelMapper requestMapper;

    @Mock
    private ModelMapper responseMapper;

    private MovementManager manager;

    @BeforeEach
    void setUp() {
        manager = new MovementManager(repository, accountRepository, mapper, rules);
        when(mapper.forRequest()).thenReturn(requestMapper);
        when(mapper.forResponse()).thenReturn(responseMapper);
    }

    @Test
    void getAllMapsRepositoryRows() {
        Movement movement = buildMovement();
        GetAllMovementsResponse response = new GetAllMovementsResponse();
        response.setId(movement.getId());
        when(repository.findAll()).thenReturn(List.of(movement));
        when(responseMapper.map(movement, GetAllMovementsResponse.class)).thenReturn(response);

        List<GetAllMovementsResponse> result = manager.getAll();

        assertEquals(1, result.size());
        assertSame(response, result.getFirst());
    }

    @Test
    void getByIdRejectsMissingMovements() {
        UUID movementId = UUID.randomUUID();
        when(repository.existsById(movementId)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> manager.getById(movementId));
    }

    @Test
    void addDepositsIncreaseTheAccountBalanceAndDescribeTheMovement() {
        CreateMovementRequest request = buildRequest(OperationType.DEPOSITED, 250.0);
        Movement mappedMovement = buildMovement();
        Account account = buildAccount(1000.0);
        CreateMovementResponse response = new CreateMovementResponse();
        response.setId(mappedMovement.getId());
        when(requestMapper.map(request, Movement.class)).thenReturn(mappedMovement);
        when(accountRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(account));
        when(repository.save(mappedMovement)).thenReturn(mappedMovement);
        when(responseMapper.map(mappedMovement, CreateMovementResponse.class)).thenReturn(response);

        CreateMovementResponse result = manager.add(request);

        assertSame(response, result);
        assertEquals(1250.0, account.getInitialBalance());
        assertEquals("Deposito de 250.0", mappedMovement.getDetail());
    }

    @Test
    void addWithdrawalsDecreaseTheAccountBalanceAndDescribeTheMovement() {
        CreateMovementRequest request = buildRequest(OperationType.WITHDRAWAL, -120.0);
        Movement mappedMovement = buildMovement();
        Account account = buildAccount(1000.0);
        when(requestMapper.map(request, Movement.class)).thenReturn(mappedMovement);
        when(accountRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(account));
        when(repository.save(mappedMovement)).thenReturn(mappedMovement);
        when(responseMapper.map(mappedMovement, CreateMovementResponse.class)).thenReturn(new CreateMovementResponse());

        manager.add(request);

        assertEquals(880.0, account.getInitialBalance());
        assertEquals("Retiro de 120.0", mappedMovement.getDetail());
    }

    @Test
    void addNeutralMovementsKeepTheAccountBalanceAndDescribeTheMovement() {
        CreateMovementRequest request = buildRequest(OperationType.NEUTRAL, 0.0);
        Movement mappedMovement = buildMovement();
        Account account = buildAccount(1000.0);
        when(requestMapper.map(request, Movement.class)).thenReturn(mappedMovement);
        when(accountRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(account));
        when(repository.save(mappedMovement)).thenReturn(mappedMovement);
        when(responseMapper.map(mappedMovement, CreateMovementResponse.class)).thenReturn(new CreateMovementResponse());

        manager.add(request);

        assertEquals(1000.0, account.getInitialBalance());
        assertEquals("No se hicieron movimientos, monto 0.0", mappedMovement.getDetail());
    }

    @Test
    void addRethrowsBadRequestExceptionsWithoutMutatingThem() {
        CreateMovementRequest request = buildRequest(OperationType.DEPOSITED, 100.0);
        when(requestMapper.map(request, Movement.class)).thenThrow(new BadRequestException("invalid payload", HttpStatus.BAD_REQUEST.toString()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> manager.add(request));

        assertEquals("invalid payload", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exception.getCode());
    }

    @Test
    void updateSavesTheMappedMovementWithTheRequestedIdentifier() {
        UUID movementId = UUID.randomUUID();
        UpdateMovementRequest request = new UpdateMovementRequest();
        Movement mappedMovement = buildMovement();
        UpdateMovementResponse response = new UpdateMovementResponse();
        response.setId(movementId);
        when(repository.existsById(movementId)).thenReturn(true);
        when(requestMapper.map(request, Movement.class)).thenReturn(mappedMovement);
        when(responseMapper.map(mappedMovement, UpdateMovementResponse.class)).thenReturn(response);

        assertSame(response, manager.update(movementId, request));
        assertEquals(movementId, mappedMovement.getId());
        verify(repository).save(mappedMovement);
    }

    @Test
    void deleteRemovesExistingMovements() {
        UUID movementId = UUID.randomUUID();
        when(repository.existsById(movementId)).thenReturn(true);

        manager.delete(movementId);

        verify(repository).deleteById(movementId);
    }

    private CreateMovementRequest buildRequest(OperationType operationType, double value) {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setTransactionType(AccountType.Ahorro);
        request.setAccountNumber("ACC-001");
        request.setInitialBalance(1000.0);
        request.setOperationType(operationType);
        request.setTransactionValue(value);
        request.setStatus(true);
        return request;
    }

    private Movement buildMovement() {
        Movement movement = new Movement();
        movement.setId(UUID.randomUUID());
        movement.setAccountNumber("ACC-001");
        movement.setInitialBalance(1000.0);
        movement.setTransactionType("Ahorro");
        movement.setTransactionValue(250.0);
        movement.setStatus(true);
        return movement;
    }

    private Account buildAccount(double balance) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setAccountNumber("ACC-001");
        account.setInitialBalance(balance);
        account.setStatus(true);
        account.setAccountType(AccountType.Ahorro);
        account.setPersonId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        return account;
    }
}
