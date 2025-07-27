package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<CreateUserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        log.info("Attempting to create user with username {}", request.getUsername());

        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            log.warn("User creation failed. Username {} already exists.", u.getUsername());
            throw new IllegalArgumentException("Username " + request.getUsername() + " is already taken.");
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User with username {} successfully created", savedUser.getUsername());

        return convertToUserResponse(savedUser);
    }

    @Transactional
    public CreateUserResponse updateUser(UUID userId, CreateUserRequest request) {
        log.info("Attempting to update user with username {}", request.getUsername());

        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), userId)) {
            log.warn("User updated failed. Username {} already exists.", request.getUsername());
            throw new IllegalArgumentException("Username " + request.getUsername() + " is already taken.");
        }

        User user = userRepository.findByIdEquals(userId).orElse(null);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User with username {} successfully updated", savedUser.getUsername());

        return convertToUserResponse(savedUser);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        log.info("Attempting to delete user with id {}", userId);

        if (!userRepository.existsById(userId)) {
            log.warn("User deleted failed, user with id {} doens't exists.", userId);
            throw new ResourceNotFoundException("User with id " + userId + " doesn't exists.");
        }

        userRepository.deleteById(userId);
        log.info("User with id {} successfully deleted.", userId);
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
