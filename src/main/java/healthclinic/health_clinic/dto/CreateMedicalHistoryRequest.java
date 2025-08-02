package healthclinic.health_clinic.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedicalHistoryRequest {

    @NotNull(message = "{medicalHistory.patientId.notnull}")
    private UUID patientId;

    @NotBlank(message = "{medicalHistory.anamnesis.notblank}")
    @Size(min = 3, max = 1024, message = "{medicalHistory.anamnesis.size}")
    private String anamnesis;

    @NotBlank(message = "{medicalHistory.bodyCheck.notblank}")
    @Size(min = 3, max = 255, message = "{medicalHistory.bodyCheck.size}")
    private String bodyCheck;

    @NotBlank(message = "{medicalHistory.diagnose.notblank}")
    @Size(min = 3, max = 512, message = "{medicalHistory.diagnose.size}")
    private String diagnose;

    @NotBlank(message = "{medicalHistory.therapy.notblank}")
    @Size(min = 3, max = 255, message = "{medicalHistory.therapy.size}")
    private String therapy;

}
