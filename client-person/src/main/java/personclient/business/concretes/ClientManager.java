package personclient.business.concretes;

import lombok.AllArgsConstructor;
import personclient.business.abstracts.ClientService;
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

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientManager implements ClientService {

    private final ClientRepository repo;
    private final ModelMapperService mapper;
    private final ClientBusinessRules rules;

    @Override
    public List<GetAllClientsResponse> getAll() {
        var todos = repo.findAll();
        return todos
                .stream()
                .map(todo -> mapper.forResponse().map(todo, GetAllClientsResponse.class))
                .toList();
    }

    @Override
    public GetClientResponse getById(UUID id) {
        if (!repo.existsById(id)) {
            throw new ClientNotFoundException(
                    "Client with id " + id + " does not exists");
        }
        var todo = repo.findById(id).orElseThrow();
        return mapper.forResponse().map(todo, GetClientResponse.class);
    }

    @Override
    public CreateClientResponse add(CreateClientRequest todoRequest) {
        try {
            var todo = mapper.forRequest().map(todoRequest, Client.class);
            var createdTodo = repo.save(todo);
            return mapper.forResponse().map(createdTodo, CreateClientResponse.class);
        } catch (BadRequestException e) {
            System.out.println("=====================================");
            System.out.println("=====================================");
            throw new BadRequestException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UpdateClientResponse update(UUID id, UpdateClientRequest todoRequest) {
        if (!repo.existsById(id)) {
            throw new ClientNotFoundException(
                    "Client with id " + id + " does not exists");
        }
        var todo = mapper.forRequest().map(todoRequest, Client.class);
        todo.setId(id);
        repo.save(todo);
        return mapper.forResponse().map(todo, UpdateClientResponse.class);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ClientNotFoundException(
                    "Client with id " + id + " does not exists");
        }
        repo.deleteById(id);
    }
}
