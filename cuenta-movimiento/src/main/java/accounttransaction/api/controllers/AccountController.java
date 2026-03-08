package accounttransaction.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import accounttransaction.business.abstracts.AccountService;
import accounttransaction.business.dto.requests.create.CreateAccountRequest;
import accounttransaction.business.dto.requests.update.UpdateAccountRequest;
import accounttransaction.business.dto.responses.create.CreateAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllAccountsResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.update.UpdateAccountResponse;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cuentas")
public class AccountController {
    private final AccountService service;

    @GetMapping
    public List<GetAllAccountsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetAccountResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccountResponse add(@RequestBody @NotNull @Validated CreateAccountRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateAccountResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateAccountRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
