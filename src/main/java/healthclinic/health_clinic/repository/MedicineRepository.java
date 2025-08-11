package healthclinic.health_clinic.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Query("SELECT m.medicineCode FROM Medicine m WHERE m.medicineCode LIKE :pattern ORDER BY m.medicineCode DESC LIMIT 1")
    String findLastMedicineCodeByPattern(String pattern);

    Optional<Medicine> findByNameEquals(String name);
}
