package healthclinic.health_clinic.dto;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import healthclinic.health_clinic.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
public class CreateUserRequestTest {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUserDto() {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("user 1");
        user.setPassword("password");

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(user);
        Assertions.assertThat(violations).isEmpty();

    }

    @Test
    void createUserErrorDto() {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("");
        user.setPassword("");

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(user);
        Assertions.assertThat(violations).extracting(v -> v.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("username", "Username harus diisi"),
                        tuple("password", "Password harus diisi"),
                        tuple("password", "Panjang password harus diantara 3 dan 255 karakter"),
                        tuple("username", "Panjang username harus diantara 3 dan 255 karakter"));
    }

}
