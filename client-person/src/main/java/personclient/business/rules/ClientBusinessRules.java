package personclient.business.rules;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import personclient.entities.ClientNotFoundException;
import personclient.repository.ClientRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientBusinessRules {
    private final ClientRepository repository;



    public void checkIfTodoExists(UUID id) {
        if (!repository.existsById(id)) {
            // log the id
            System.out.println(id);

            // TODO: BusinessException
            throw new ClientNotFoundException(
                    "Todo with id " + id + " does not exists");
        }
    }


}
