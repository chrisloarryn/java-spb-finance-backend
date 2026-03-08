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

    @JsonProperty("numero")
    private String accountNumber;

    @JsonProperty("saldoinicial")
    private Double initialBalance;

    @JsonProperty("estado")
    private Boolean status;

    @JsonProperty("tipo")
    private AccountType accountType;

    @JsonProperty("persona")
    private UUID personId;

    @JsonProperty("fecha_creacion")
    private Date createdAt;
}
