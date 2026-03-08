package accounttransaction.business.dto.requests.update;

import accounttransaction.entities.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovementRequest {
    @JsonProperty("transactionType")
    @NonNull
    private AccountType transactionType = AccountType.SAVINGS;

    @JsonProperty("initialBalance")
    @NonNull
    private Double initialBalance;

    @JsonProperty("transactionValue")
    @NonNull
    private Double transactionValue;

    @JsonProperty("status")
    @NonNull
    private Boolean status = true;
}
