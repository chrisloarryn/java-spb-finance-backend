package personclient.api.controllers;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import personclient.business.abstracts.ClientService;
import personclient.business.dto.requests.create.CreateClientRequest;
import personclient.business.dto.requests.update.UpdateClientRequest;
import personclient.business.dto.responses.create.CreateClientResponse;
import personclient.business.dto.responses.get.GetAllClientsResponse;
import personclient.business.dto.responses.get.GetClientResponse;
import personclient.business.dto.responses.update.UpdateClientResponse;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTests {

    @Mock
    private ClientService service;

    @InjectMocks
    private ClientController controller;

    @Test
    void getAllDelegatesToTheService() {
        List<GetAllClientsResponse> response = List.of(new GetAllClientsResponse());
        when(service.getAll()).thenReturn(response);

        assertSame(response, controller.getAll());
        verify(service).getAll();
    }

    @Test
    void getByIdDelegatesToTheService() {
        UUID clientId = UUID.randomUUID();
        GetClientResponse response = new GetClientResponse();
        when(service.getById(clientId)).thenReturn(response);

        assertSame(response, controller.getById(clientId));
        verify(service).getById(clientId);
    }

    @Test
    void addDelegatesToTheService() {
        CreateClientRequest request = new CreateClientRequest();
        CreateClientResponse response = new CreateClientResponse();
        when(service.add(request)).thenReturn(response);

        assertSame(response, controller.add(request));
        verify(service).add(request);
    }

    @Test
    void updateDelegatesToTheService() {
        UUID clientId = UUID.randomUUID();
        UpdateClientRequest request = new UpdateClientRequest();
        UpdateClientResponse response = new UpdateClientResponse();
        when(service.update(clientId, request)).thenReturn(response);

        assertSame(response, controller.update(clientId, request));
        verify(service).update(clientId, request);
    }

    @Test
    void deleteDelegatesToTheService() {
        UUID clientId = UUID.randomUUID();

        controller.delete(clientId);

        verify(service).delete(clientId);
    }
}
