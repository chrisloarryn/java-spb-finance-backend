package accounttransaction.business.dto.responses.create;

import com.fasterxml.jackson.annotation.JsonProperty;

import accounttransaction.entities.enums.AccountType;
import accounttransaction.entities.enums.Priority;
import accounttransaction.entities.enums.State;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("numerocuenta")
    private String accountNumber;

    @JsonProperty("saldoinicial")
    private Double initialBalance;

    @JsonProperty("estado")
    private Boolean status = true;

    @JsonProperty("tipo")
    private AccountType accountType = AccountType.Ahorro;

    @JsonProperty("persona")
    private UUID personId;
}
