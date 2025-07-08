package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);

    List<CreateUserResponse> findAllUsers();
}
