package healthclinic.health_clinic.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
