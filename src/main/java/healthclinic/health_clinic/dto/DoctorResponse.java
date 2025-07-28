package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import healthclinic.health_clinic.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private UUID id;

    private String fullName;

    private String sip;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private Integer age;

    private String gender;

    private String phone;

    private Address address;

    private User user;

    private Instant createdAt;

    private Instant updatedAt;

}
