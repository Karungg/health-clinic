package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserResponse {
    private UUID id;

    private String username;

    private String password;

    private Instant createdAt;

    private Instant updatedAt;
}
