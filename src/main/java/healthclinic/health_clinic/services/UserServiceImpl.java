package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        log.info("Attempting to create user with username {}", request.getUsername());

        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            log.warn("User creation failed. Username {} already exists.", u.getUsername());
            throw new IllegalArgumentException("Username " + request.getUsername() + " is already taken.");
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);
        log.info("User with username {} successfully created", savedUser.getUsername());

        return convertToUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<CreateUserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    private CreateUserResponse convertToUserResponse(User user) {
        return new CreateUserResponse(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
