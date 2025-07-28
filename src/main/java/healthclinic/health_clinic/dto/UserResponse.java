package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.util.UUID;

import healthclinic.health_clinic.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;

    private String username;

    private String password;

    private Role role;

    private Instant createdAt;

    private Instant updatedAt;
}
