package accounttransaction.business.dto.responses.update;

import accounttransaction.entities.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("initialBalance")
    private Double initialBalance;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("accountType")
    private AccountType accountType;

    @JsonProperty("clientId")
    private UUID personId;

    @JsonProperty("createdAt")
    private Date createdAt;
}
