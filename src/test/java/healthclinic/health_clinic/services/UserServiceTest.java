package healthclinic.health_clinic.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("user 1");
        request.setPassword("password");

        CreateUserResponse user = userService.createUser(request);
        User userFromRepo = userRepository.findByUsername("user 1").orElse(null);

        Assertions.assertThat(user.getId()).isEqualTo(userFromRepo.getId());
    }
}
