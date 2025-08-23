package healthclinic.health_clinic.dto;

import java.time.LocalDate;

import healthclinic.health_clinic.Enums.BloodType;
import healthclinic.health_clinic.Enums.Gender;
import healthclinic.health_clinic.validator.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequest {

    @Valid
    @NotNull(message = "{patient.address.notnull}")
    private Address address;

    @Valid
    @NotNull(message = "{patient.user.notnull}")
    private CreateUserRequest user;

    @NotBlank(message = "{patient.fullname.notblank}")
    @Size(min = 3, message = "{patient.fullname.size}")
    private String fullName;

    @NotBlank(message = "{patient.nik.notblank}")
    @Pattern(regexp = "[0-9]+", message = "{patient.nik.pattern}")
    @Size(min = 16, max = 16, message = "{patient.nik.size}")
    private String nik;

    @PastOrPresent(message = "{patient.dateOfBirth.pastorpresent}")
    @NotNull(message = "{patient.dateOfBirth.notnull}")
    private LocalDate dateOfBirth;

    @NotNull(message = "{patient.age.notnull}")
    @Min(value = 1, message = "{patient.age.min}")
    @Max(value = 999, message = "{patient.age.max}")
    private Integer age;

    @NotBlank(message = "{patient.phone.notblank}")
    @Size(min = 9, max = 14, message = "{patient.phone.size}")
    private String phone;

    @ValueOfEnum(enumClass = Gender.class, message = "{patient.gender.enum}")
    @NotBlank(message = "{patient.gender.notblank}")
    @Size(max = 6, message = "{patient.gender.size}")
    private String gender;

    @NotBlank(message = "{patient.job.notblank}")
    @Size(min = 3, message = "{patient.job.size}")
    private String job;

    @NotBlank(message = "{patient.placeofbirth.notblank}")
    @Size(min = 3, message = "{patient.placeofbirth.size}")
    private String placeOfBirth;

    @NotNull(message = "{patient.weight.notnull}")
    @Min(value = 1, message = "{patient.weight.min}")
    @Max(value = 999, message = "{patient.weight.max}")
    private Integer weight;

    @NotNull(message = "{patient.height.notnull}")
    @Min(value = 1, message = "{patient.height.min}")
    @Max(value = 999, message = "{patient.height.max}")
    private Integer height;

    @ValueOfEnum(enumClass = BloodType.class, message = "{patient.bloodtype.enum}")
    @NotBlank(message = "{patient.bloodtype.notblank}")
    @Size(max = 3, message = "{patient.bloodtype.size}")
    private String bloodType;
}
