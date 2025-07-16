package healthclinic.health_clinic.dto;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePatientRequest {

    @NotBlank(message = "{patient.fullname.notblank}")
    @Size(min = 3, message = "{patient.fullname.size}")
    private String fullName;

    @NotBlank(message = "{patient.nik.notblank}")
    @Size(min = 16, max = 16, message = "{patient.nik.size}")
    private Integer nik;

    @NotBlank(message = "{patient.dateofbirth.notblank}")
    private Date dateOfBirth;

    @NotBlank(message = "{patient.age.notblank}")
    @Size(max = 3, message = "{patient.age.size}")
    private Integer age;

    @NotBlank(message = "{patient.address.notblank}")
    private Address address;

    @NotBlank(message = "{patient.phone.notblank}")
    @Size(min = 9, max = 14, message = "{patient.phone.size}")
    private Integer phone;

    @NotBlank(message = "{patient.gender.notblank}")
    @Size(max = 6, message = "{patient.gender.size}")
    private String gender;

    @NotBlank(message = "{patient.job.notblank}")
    @Size(min = 3, message = "{patient.job.size}")
    private String job;

    @NotBlank(message = "{patient.placeofbirth.notblank}")
    @Size(min = 3, message = "{patient.placeofbirth.size}")
    private String placeOfBirth;

    @NotBlank(message = "{patient.weight.notblank}")
    @Size(max = 3, message = "{patient.weight.size}")
    private Integer weight;

    @NotBlank(message = "{patient.height.notblank}")
    @Size(max = 3, message = "{patient.height.size}")
    private Integer height;

    @NotBlank(message = "{patient.bloodtype.notblank}")
    @Size(max = 2, message = "{patient.bloodtype.size}")
    private String bloodType;

    @NotBlank(message = "{patient.userid.notblank}")
    private UUID userId;
}
