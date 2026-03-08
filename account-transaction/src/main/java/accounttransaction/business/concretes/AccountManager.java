package accounttransaction.business.concretes;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import accounttransaction.business.abstracts.AccountService;
import accounttransaction.business.dto.requests.create.CreateAccountRequest;
import accounttransaction.business.dto.requests.update.UpdateAccountRequest;
import accounttransaction.business.dto.responses.create.CreateAccountResponse;
import accounttransaction.business.dto.responses.get.GetAllAccountsResponse;
import accounttransaction.business.dto.responses.get.GetAccountResponse;
import accounttransaction.business.dto.responses.update.UpdateAccountResponse;
import accounttransaction.business.rules.AccountBusinessRules;
import accounttransaction.entities.Account;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.exceptions.BadRequestException;
import accounttransaction.repository.AccountRepository;
import accounttransaction.utils.mappers.ModelMapperService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountManager implements AccountService {

    private final AccountRepository repo;
    private final ModelMapperService mapper;
    private final AccountBusinessRules rules;

    @Override
    public List<GetAllAccountsResponse> getAll() {
        var todos = repo.findAll();
        return todos
                .stream()
                .map(todo -> mapper.forResponse().map(todo, GetAllAccountsResponse.class))
                .toList();
    }

    @Override
    public GetAccountResponse getById(UUID id) {
        if (!repo.existsById(id)) {
            throw new AccountNotFoundException(
                    "Account with id " + id + " does not exists");
        }
        var todo = repo.findById(id).orElseThrow();
        return mapper.forResponse().map(todo, GetAccountResponse.class);
    }

    @Override
    public GetAccountResponse getByAccountNumber(String accountNumber) {
        var todo = repo.findByAccountNumber(accountNumber).orElseThrow();
        return mapper.forResponse().map(todo, GetAccountResponse.class);
    }

    @Override
    public CreateAccountResponse add(CreateAccountRequest todoRequest) {
        try {
            var todo = mapper.forRequest().map(todoRequest, Account.class);
            todo.setId(null);
            var createdTodo = repo.save(todo);
            return mapper.forResponse().map(createdTodo, CreateAccountResponse.class);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UpdateAccountResponse update(UUID id, UpdateAccountRequest todoRequest) {
        if (!repo.existsById(id)) {
            throw new AccountNotFoundException(
                    "Account with id " + id + " does not exists");
        }
        var todo = mapper.forRequest().map(todoRequest, Account.class);
        todo.setId(id);
        repo.save(todo);
        return mapper.forResponse().map(todo, UpdateAccountResponse.class);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new AccountNotFoundException(
                    "Account with id " + id + " does not exists");
        }
        repo.deleteById(id);
    }

    @Override
    public boolean hasEnoughBalance(String accountNumber, double amount) {
        Account accountFound = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " does not exist"));

        double absoluteAmount = Math.abs(amount);

        System.out.println("==============================");
        System.out.println(String.format("Current account balance: %s", accountFound.getInitialBalance()));
        System.out.println(String.format("Absolute transaction amount: %s", absoluteAmount));
        System.out.println("==============================");

        // Check whether the current balance covers the requested amount.
        return accountFound.getInitialBalance() >= absoluteAmount;
    }
}
