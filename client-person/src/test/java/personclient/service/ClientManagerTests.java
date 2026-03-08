package personclient.service;

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
import personclient.business.concretes.ClientManager;
import personclient.business.dto.requests.create.CreateClientRequest;
import personclient.business.dto.requests.update.UpdateClientRequest;
import personclient.business.dto.responses.create.CreateClientResponse;
import personclient.business.dto.responses.get.GetAllClientsResponse;
import personclient.business.dto.responses.get.GetClientResponse;
import personclient.business.dto.responses.update.UpdateClientResponse;
import personclient.business.rules.ClientBusinessRules;
import personclient.entities.Client;
import personclient.entities.ClientNotFoundException;
import personclient.exceptions.BadRequestException;
import personclient.repository.ClientRepository;
import personclient.utils.mappers.ModelMapperService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClientManagerTests {

    @Mock
    private ClientRepository repository;

    @Mock
    private ModelMapperService mapper;

    @Mock
    private ClientBusinessRules rules;

    @Mock
    private ModelMapper requestMapper;

    @Mock
    private ModelMapper responseMapper;

    private ClientManager manager;

    @BeforeEach
    void setUp() {
        manager = new ClientManager(repository, mapper, rules);
        when(mapper.forRequest()).thenReturn(requestMapper);
        when(mapper.forResponse()).thenReturn(responseMapper);
    }

    @Test
    void getAllMapsRepositoryRows() {
        Client client = buildClient();
        GetAllClientsResponse response = new GetAllClientsResponse();
        response.setId(client.getId());
        when(repository.findAll()).thenReturn(List.of(client));
        when(responseMapper.map(client, GetAllClientsResponse.class)).thenReturn(response);

        List<GetAllClientsResponse> result = manager.getAll();

        assertEquals(1, result.size());
        assertSame(response, result.getFirst());
    }

    @Test
    void getByIdReturnsTheMappedClientWhenItExists() {
        UUID clientId = UUID.randomUUID();
        Client client = buildClient();
        GetClientResponse response = new GetClientResponse();
        response.setId(clientId);
        when(repository.existsById(clientId)).thenReturn(true);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(responseMapper.map(client, GetClientResponse.class)).thenReturn(response);

        GetClientResponse result = manager.getById(clientId);

        assertSame(response, result);
    }

    @Test
    void getByIdRejectsMissingClients() {
        UUID clientId = UUID.randomUUID();
        when(repository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> manager.getById(clientId));
    }

    @Test
    void addPersistsTheMappedClient() {
        CreateClientRequest request = new CreateClientRequest();
        Client mappedClient = buildClient();
        Client persistedClient = buildClient();
        CreateClientResponse response = new CreateClientResponse();
        response.setId(persistedClient.getId());
        when(requestMapper.map(request, Client.class)).thenReturn(mappedClient);
        when(repository.save(mappedClient)).thenReturn(persistedClient);
        when(responseMapper.map(persistedClient, CreateClientResponse.class)).thenReturn(response);

        CreateClientResponse result = manager.add(request);

        assertSame(response, result);
        verify(repository).save(mappedClient);
    }

    @Test
    void addRethrowsBadRequestExceptionsWithoutChangingThePayload() {
        CreateClientRequest request = new CreateClientRequest();
        when(requestMapper.map(request, Client.class)).thenThrow(new BadRequestException("invalid payload", "400"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> manager.add(request));

        assertEquals("invalid payload", exception.getMessage());
        assertEquals("400", exception.getCode());
    }

    @Test
    void updateRejectsMissingClients() {
        UUID clientId = UUID.randomUUID();
        when(repository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> manager.update(clientId, new UpdateClientRequest()));
    }

    @Test
    void updateSavesTheMappedClientWithTheRequestedIdentifier() {
        UUID clientId = UUID.randomUUID();
        UpdateClientRequest request = new UpdateClientRequest();
        Client mappedClient = buildClient();
        UpdateClientResponse response = new UpdateClientResponse();
        response.setId(clientId);
        when(repository.existsById(clientId)).thenReturn(true);
        when(requestMapper.map(request, Client.class)).thenReturn(mappedClient);
        when(responseMapper.map(mappedClient, UpdateClientResponse.class)).thenReturn(response);

        UpdateClientResponse result = manager.update(clientId, request);

        assertSame(response, result);
        assertEquals(clientId, mappedClient.getId());
        verify(repository).save(mappedClient);
    }

    @Test
    void deleteRemovesExistingClients() {
        UUID clientId = UUID.randomUUID();
        when(repository.existsById(clientId)).thenReturn(true);

        manager.delete(clientId);

        verify(repository).deleteById(clientId);
    }

    private Client buildClient() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("Jane Doe");
        client.setClient_id("jane-001");
        client.setPassword("secret");
        client.setGender("F");
        client.setAge(30);
        client.setEmail_identifier("jane@example.com");
        client.setAddress("Street 123");
        client.setPhone_number("+56912345678");
        client.setStatus("ACTIVE");
        return client;
    }
}
