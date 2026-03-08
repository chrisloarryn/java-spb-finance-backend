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

    @JsonProperty("fecha_creacion")
    private Date createdAt;
}
