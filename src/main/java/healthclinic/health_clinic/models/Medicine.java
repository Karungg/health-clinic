package healthclinic.health_clinic.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import healthclinic.health_clinic.Enums.MedicineCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;

@EntityListeners({ AuditingEntityListener.class })
@Entity
@Table(name = "medicines")
@Data
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "medicine_code", nullable = false, unique = true)
    private String medicineCode;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicineCategory category;

    @Column(nullable = false)
    private String form;

    @Column(nullable = false)
    private String strength;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String batch_number;

    @Column(nullable = false)
    private LocalDate expiry_date;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String storage_conditions;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
