package healthclinic.health_clinic.models;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import healthclinic.health_clinic.converter.AddressAttributeConverter;
import healthclinic.health_clinic.dto.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private Date dateOfBirth;

    @Column(name = "age", nullable = false, length = 3)
    private Integer age;

    @Convert(converter = AddressAttributeConverter.class)
    @Column(name = "address", length = 500, nullable = false)
    private Address address;

    @Column(name = "phone", nullable = false, length = 14, unique = true)
    private String phone;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "weight", nullable = false, length = 3)
    private Integer weight;

    @Column(name = "height", nullable = false, length = 3)
    private Integer height;

    @Column(name = "blood_type", nullable = false, length = 2)
    private String bloodType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
