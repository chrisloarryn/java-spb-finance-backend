package accounttransaction.business.abstracts;

import accounttransaction.business.dto.requests.create.CreateMovementRequest;
import accounttransaction.business.dto.requests.update.UpdateMovementRequest;
import accounttransaction.business.dto.responses.create.CreateMovementResponse;
import accounttransaction.business.dto.responses.get.GetAllMovementsResponse;
import accounttransaction.business.dto.responses.get.GetMovementResponse;
import accounttransaction.business.dto.responses.get.GetReportResponse;
import accounttransaction.business.dto.responses.update.UpdateMovementResponse;

import java.util.List;
import java.util.UUID;

public interface MovementService {
    List<GetAllMovementsResponse> getAll();

    GetMovementResponse getById(UUID id);

    CreateMovementResponse add(CreateMovementRequest todo);

    UpdateMovementResponse update(UUID id, UpdateMovementRequest todo);

    void delete(UUID id);
}
