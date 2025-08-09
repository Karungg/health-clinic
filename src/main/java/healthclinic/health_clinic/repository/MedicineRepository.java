package healthclinic.health_clinic.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {

}
