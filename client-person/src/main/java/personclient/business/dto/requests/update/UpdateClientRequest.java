package personclient.business.dto.requests.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import personclient.entities.enums.Priority;
import personclient.entities.enums.State;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientRequest {
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
