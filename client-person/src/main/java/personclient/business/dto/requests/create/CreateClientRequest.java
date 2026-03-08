package personclient.business.dto.requests.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import personclient.entities.Client;
import personclient.entities.Person;
import personclient.entities.enums.Priority;
import personclient.entities.enums.State;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {
    @NotNull(message = "The name field cannot be null")
    @Size(min = 3, max = 50, message = "The name field must contain between 3 and 50 characters")
    @NonNull
    @JsonProperty("name")
    private String name;

    @NonNull
    @JsonProperty("clientId")
    private String client_id;

    @NonNull
    @JsonProperty("password")
    private String password;

    @NonNull
    @JsonProperty("gender")
    private String gender;

    @NonNull
    @JsonProperty("age")
    private int age;

    @NonNull
    @JsonProperty("identifier")
    private String email_identifier;

    @NonNull
    @JsonProperty("address")
    private String address;

    @NonNull
    @JsonProperty("phoneNumber")
    private String phone_number;

    @JsonProperty("status")
    private String status = State.ACTIVE.getState();

    @JsonProperty("createdAt")
    private Date createdAt = new Date();
}
