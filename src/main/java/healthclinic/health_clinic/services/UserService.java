package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;

public interface UserService {
    List<CreateUserResponse> findAllUsers();

    CreateUserResponse createUser(CreateUserRequest request);

    CreateUserResponse updateUser(UUID userId, CreateUserRequest request);

    void deleteUser(UUID userId);
}
