package accounttransaction.business.dto.requests.create;

import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CreateMovementRequest {
    @JsonProperty("transactionType")
    @NonNull
    private AccountType transactionType = AccountType.SAVINGS;

    @JsonProperty("accountNumber")
    @NonNull
    private String accountNumber;

    // @JsonProperty("initialBalance")
    // @NonNull
    @JsonIgnore
    private Double initialBalance;

    @JsonIgnore
    private OperationType operationType;

    @JsonProperty("transactionValue")
    @NonNull
    private Double transactionValue;

    @JsonProperty("status")
    private Boolean status = true;
}
