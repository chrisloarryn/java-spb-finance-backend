package accounttransaction.business.dto.responses.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.Priority;
import accounttransaction.entities.enums.State;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountResponse {
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
