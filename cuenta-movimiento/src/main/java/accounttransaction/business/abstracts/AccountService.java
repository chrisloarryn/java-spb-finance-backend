package accounttransaction.business.abstracts;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import accounttransaction.business.dto.requests.create.CreateAccountRequest;
import accounttransaction.business.dto.requests.update.UpdateAccountRequest;
import accounttransaction.business.dto.responses.create.CreateAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllAccountsResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.update.UpdateAccountResponse;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<GetAllAccountsResponse> getAll();

    GetAccountResponse getById(UUID id);
    GetAccountResponse getByAccountNumber(String accountNumber);

    CreateAccountResponse add(CreateAccountRequest todo);

    UpdateAccountResponse update(UUID id, UpdateAccountRequest todo);

    void delete(UUID id);

    boolean hasEnoughBalance(String accountNumber, double amount);
}
