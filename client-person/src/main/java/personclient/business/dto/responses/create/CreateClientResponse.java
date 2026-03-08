package personclient.business.dto.responses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import personclient.entities.enums.Priority;
import personclient.entities.enums.State;

import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientResponse {
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
}
