package healthclinic.health_clinic.models;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import healthclinic.health_clinic.converter.AddressAttributeConverter;
import healthclinic.health_clinic.dto.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;

@EntityListeners({ AuditingEntityListener.class })
@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "nik", nullable = false, unique = true)
    private String nik;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Convert(converter = AddressAttributeConverter.class)
    @Column(name = "address", length = 500, nullable = false)
    private Address address;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "blood_type", nullable = false, length = 3)
    private String bloodType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", unique = true)
    private User user;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "patient", orphanRemoval = true, cascade = CascadeType.ALL)
    private MedicalHistory medicalHistory;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
