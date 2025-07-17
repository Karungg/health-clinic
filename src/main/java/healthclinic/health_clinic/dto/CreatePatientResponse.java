package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePatientResponse {

    private UUID id;

    private String fullName;

    private String nik;

    private LocalDate dateOfBirth;

    private String age;

    private Address address;

    private String phone;

    private String gender;

    private String job;

    private String placeOfBirth;

    private String weight;

    private String height;

    private String bloodType;

    private UUID userId;

    private Instant createdAt;

    private Instant updatedAt;
}
