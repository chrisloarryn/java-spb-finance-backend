package accounttransaction.business.dto.requests.create;

import com.fasterxml.jackson.annotation.JsonProperty;

import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.Priority;
import accounttransaction.entities.enums.State;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @JsonProperty("accountNumber")
    @NonNull
    private String accountNumber;

    @JsonProperty("initialBalance")
    @NonNull
    private Double initialBalance;

    @JsonProperty("status")
    @NonNull
    private Boolean status;

    @JsonProperty("accountType")
    @NonNull
    private AccountType accountType = AccountType.SAVINGS;

    @JsonProperty("clientId")
    @NonNull
    private UUID personId;
}
