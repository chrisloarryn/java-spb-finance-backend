package accounttransaction.business.dto.responses.update;

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
public class UpdateMovementResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("initialBalance")
    private Double initialBalance;

    @JsonProperty("transactionValue")
    private Double transactionValue;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("createdAt")
    private Date createdAt;
}
