package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);

    CreateUserResponse updateUser(UUID userId, CreateUserRequest request);

    List<CreateUserResponse> findAllUsers();

    boolean isExistsByUsernameNotId(String username, UUID userId);

    void deleteUser(UUID userId);
}
