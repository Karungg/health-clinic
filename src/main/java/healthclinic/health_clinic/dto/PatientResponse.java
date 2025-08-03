package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import healthclinic.health_clinic.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PatientResponse {

    private UUID id;

    private String fullName;

    private String nik;

    private LocalDate dateOfBirth;

    private Integer age;

    private Address address;

    private String phone;

    private String gender;

    private String job;

    private String placeOfBirth;

    private Integer weight;

    private Integer height;

    private String bloodType;

    private User user;

    private Instant createdAt;

    private Instant updatedAt;
}
