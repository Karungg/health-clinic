package healthclinic.health_clinic.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Test
    void createUser() {
        User user = new User();
        user.setUsername("user 1");
        user.setPassword("password");

        assertNotNull(user);
    }
}
