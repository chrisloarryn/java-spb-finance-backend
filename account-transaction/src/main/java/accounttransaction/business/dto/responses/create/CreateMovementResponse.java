package accounttransaction.business.dto.responses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovementResponse {
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
    @CreationTimestamp
    private Date createdAt;
}
