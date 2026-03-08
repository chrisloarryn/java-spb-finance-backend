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

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("clienteid")
    private String client_id;

    @JsonProperty("contrasena")
    private String password;

    @JsonProperty("genero")
    private String gender;

    @JsonProperty("edad")
    private int age;

    @JsonProperty("identificacion")
    private String email_identifier;

    @JsonProperty("direccion")
    private String address;

    @JsonProperty("telefono")
    private String phone_number;

    @JsonProperty("estado")
    private String status;
}
