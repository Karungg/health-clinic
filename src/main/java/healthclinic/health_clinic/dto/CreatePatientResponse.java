package healthclinic.health_clinic.dto;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePatientResponse {

    private UUID id;

    private String fullName;

    private Integer nik;

    private Date dateOfBirth;

    private Integer age;

    private Address address;

    private Integer phone;

    private String gender;

    private String job;

    private String placeOfBirth;

    private Integer weight;

    private Integer height;

    private String bloodType;

    private UUID userId;

    private Instant createdAt;

    private Instant updatedAt;
}
