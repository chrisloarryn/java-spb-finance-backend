package accounttransaction.business.rules;

import org.springframework.stereotype.Service;

import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.repository.AccountRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountBusinessRules {
    private final AccountRepository repository;



    public void checkIfTodoExists(UUID id) {
        if (!repository.existsById(id)) {
            // log the id
            System.out.println(id);

            // TODO: BusinessException
            throw new AccountNotFoundException(
                    "Todo with id " + id + " does not exists");
        }
    }


}
