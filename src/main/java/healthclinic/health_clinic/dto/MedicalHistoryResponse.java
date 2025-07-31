package healthclinic.health_clinic.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryResponse {

    private UUID id;

    private UUID patientId;

    private String anamnesis;

    private String bodyCheck;

    private String diagnose;

    private String therapy;

}
