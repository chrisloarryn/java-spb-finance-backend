package personclient.business.abstracts;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import personclient.business.dto.requests.create.CreateClientRequest;
import personclient.business.dto.requests.update.UpdateClientRequest;
import personclient.business.dto.responses.create.CreateClientResponse;
import personclient.business.dto.responses.get.GetAllClientsResponse;
import personclient.business.dto.responses.get.GetClientResponse;
import personclient.business.dto.responses.update.UpdateClientResponse;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<GetAllClientsResponse> getAll();

    GetClientResponse getById(UUID id);

    CreateClientResponse add(CreateClientRequest todo);

    UpdateClientResponse update(UUID id, UpdateClientRequest todo);

    void delete(UUID id);
}
