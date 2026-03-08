package personclient.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import personclient.business.abstracts.ClientService;
import personclient.business.dto.requests.create.CreateClientRequest;
import personclient.business.dto.requests.update.UpdateClientRequest;
import personclient.business.dto.responses.create.CreateClientResponse;
import personclient.business.dto.responses.get.GetAllClientsResponse;
import personclient.business.dto.responses.get.GetClientResponse;
import personclient.business.dto.responses.update.UpdateClientResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService service;

    @GetMapping
    public List<GetAllClientsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetClientResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateClientResponse add(@RequestBody @NotNull @Validated CreateClientRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateClientResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateClientRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
