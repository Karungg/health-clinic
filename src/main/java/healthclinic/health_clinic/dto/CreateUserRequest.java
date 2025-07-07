package healthclinic.health_clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "{user.username.notblank}")
    @Size(min = 3, max = 255, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.password.notblank}")
    @Size(min = 3, max = 255, message = "{user.password.size}")
    private String password;

}
