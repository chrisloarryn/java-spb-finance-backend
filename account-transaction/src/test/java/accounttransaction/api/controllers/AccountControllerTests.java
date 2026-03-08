package accounttransaction.api.controllers;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import accounttransaction.business.abstracts.AccountService;
import accounttransaction.business.dto.requests.create.CreateAccountRequest;
import accounttransaction.business.dto.requests.update.UpdateAccountRequest;
import accounttransaction.business.dto.responses.create.CreateAccountResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllAccountsResponse;
import accounttransaction.business.dto.responses.update.UpdateAccountResponse;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTests {

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountController controller;

    @Test
    void getAllDelegatesToTheService() {
        List<GetAllAccountsResponse> response = List.of(new GetAllAccountsResponse());
        when(service.getAll()).thenReturn(response);

        assertSame(response, controller.getAll());
        verify(service).getAll();
    }

    @Test
    void getByIdDelegatesToTheService() {
        UUID accountId = UUID.randomUUID();
        GetAccountResponse response = new GetAccountResponse();
        when(service.getById(accountId)).thenReturn(response);

        assertSame(response, controller.getById(accountId));
        verify(service).getById(accountId);
    }

    @Test
    void addDelegatesToTheService() {
        CreateAccountRequest request = new CreateAccountRequest();
        CreateAccountResponse response = new CreateAccountResponse();
        when(service.add(request)).thenReturn(response);

        assertSame(response, controller.add(request));
        verify(service).add(request);
    }

    @Test
    void updateDelegatesToTheService() {
        UUID accountId = UUID.randomUUID();
        UpdateAccountRequest request = new UpdateAccountRequest();
        UpdateAccountResponse response = new UpdateAccountResponse();
        when(service.update(accountId, request)).thenReturn(response);

        assertSame(response, controller.update(accountId, request));
        verify(service).update(accountId, request);
    }

    @Test
    void deleteDelegatesToTheService() {
        UUID accountId = UUID.randomUUID();

        controller.delete(accountId);

        verify(service).delete(accountId);
    }
}
