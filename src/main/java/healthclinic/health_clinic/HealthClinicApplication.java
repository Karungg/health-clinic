package healthclinic.health_clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HealthClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthClinicApplication.class, args);
	}

}
