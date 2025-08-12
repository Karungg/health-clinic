package healthclinic.health_clinic.models;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "transaction_details")
@Data
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, name = "current_price")
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false, referencedColumnName = "id")
    private Transaction transaction;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medicine_id", nullable = false, referencedColumnName = "id")
    private Medicine medicine;
}
