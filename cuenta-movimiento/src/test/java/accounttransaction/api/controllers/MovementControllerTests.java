package accounttransaction.api.controllers;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import accounttransaction.business.abstracts.AccountService;
import accounttransaction.business.abstracts.MovementService;
import accounttransaction.business.dto.requests.create.CreateMovementRequest;
import accounttransaction.business.dto.responses.create.CreateMovementResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllMovementsResponse;
import accounttransaction.entities.enums.OperationType;
import accounttransaction.exceptions.InsuficientBalanceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementControllerTests {

    @Mock
    private MovementService service;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private MovementController controller;

    @Test
    void getAllDelegatesToTheService() {
        List<GetAllMovementsResponse> response = List.of(new GetAllMovementsResponse());
        when(service.getAll()).thenReturn(response);

        assertSame(response, controller.getAll());
        verify(service).getAll();
    }

    @Test
    void addSetsDepositedOperationTypeAndLoadsTheInitialBalance() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setAccountNumber("ACC-001");
        request.setTransactionValue(120.0);
        GetAccountResponse account = new GetAccountResponse();
        account.setInitialBalance(500.0);
        CreateMovementResponse response = new CreateMovementResponse();
        when(accountService.getByAccountNumber("ACC-001")).thenReturn(account);
        when(service.add(request)).thenReturn(response);

        CreateMovementResponse result = controller.add(request);

        assertSame(response, result);
        assertEquals(OperationType.DEPOSITED, request.getOperationType());
        assertEquals(500.0, request.getInitialBalance());
        verify(service).add(request);
    }

    @Test
    void addRejectsWithdrawalsWithoutEnoughBalance() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setAccountNumber("ACC-001");
        request.setTransactionValue(-200.0);
        when(accountService.hasEnoughBalance("ACC-001", -200.0)).thenReturn(false);

        assertThrows(InsuficientBalanceException.class, () -> controller.add(request));

        assertEquals(OperationType.WITHDRAWAL, request.getOperationType());
        verify(service, never()).add(request);
    }

    @Test
    void addSetsNeutralOperationTypeForZeroValueTransactions() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setAccountNumber("ACC-001");
        request.setTransactionValue(0.0);
        GetAccountResponse account = new GetAccountResponse();
        account.setInitialBalance(250.0);
        CreateMovementResponse response = new CreateMovementResponse();
        when(accountService.getByAccountNumber("ACC-001")).thenReturn(account);
        when(service.add(request)).thenReturn(response);

        CreateMovementResponse result = controller.add(request);

        assertSame(response, result);
        assertEquals(OperationType.NEUTRAL, request.getOperationType());
        assertEquals(250.0, request.getInitialBalance());
    }
}
