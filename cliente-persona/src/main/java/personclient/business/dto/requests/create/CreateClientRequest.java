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
    @NotNull(message = "El campo name no puede ser nulo")
    @Size(min = 3, max = 50, message = "El campo name debe tener entre 3 y 50 caracteres")
    @NonNull
    @JsonProperty("nombre")
    private String name;

    @NonNull
    @JsonProperty("clienteid")
    private String client_id;

    @NonNull
    @JsonProperty("contrasena")
    private String password;

    @NonNull
    @JsonProperty("genero")
    private String gender;

    @NonNull
    @JsonProperty("edad")
    private int age;

    @NonNull
    @JsonProperty("identificacion")
    private String email_identifier;

    @NonNull
    @JsonProperty("direccion")
    private String address;

    @NonNull
    @JsonProperty("telefono")
    private String phone_number;

    @JsonProperty("estado")
    private String status = State.ACTIVE.getState();

    @JsonProperty("fecha_creacion")
    private Date createdAt = new Date();
}
