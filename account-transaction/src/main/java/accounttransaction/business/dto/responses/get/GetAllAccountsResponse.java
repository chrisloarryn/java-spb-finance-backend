package accounttransaction.business.dto.responses.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.Priority;
import accounttransaction.entities.enums.State;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllAccountsResponse {
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
