package personclient.business.dto.responses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import personclient.entities.enums.Priority;
import personclient.entities.enums.State;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllClientsResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("clientId")
    private String client_id;

    @JsonProperty("password")
    private String password;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    private int age;

    @JsonProperty("identifier")
    private String email_identifier;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phoneNumber")
    private String phone_number;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private Date createdAt;
}
