package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.UserResponse;

public interface UserService {
    List<UserResponse> findAllUsers();

    UserResponse getUserById(UUID userId);

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UUID userId, CreateUserRequest request);

    void deleteUser(UUID userId);
}
