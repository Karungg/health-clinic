package healthclinic.health_clinic.models;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@EntityListeners({ AuditingEntityListener.class })
@Entity
@Table(name = "medical_histories")
@Data
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "anamnesis", nullable = false, length = 1024)
    private String anamnesis;

    @Column(name = "body_check", nullable = false)
    private String bodyCheck;

    @Column(name = "diagnose", nullable = false, length = 512)
    private String diagnose;

    @Column(name = "therapy", nullable = false)
    private String therapy;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true, referencedColumnName = "id")
    private Patient patient;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
