package healthclinic.health_clinic.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Patient findByFullNameEquals(String fullName);

    Optional<Patient> findByNik(String nik);

    Optional<Patient> findByPhone(String phone);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM patients WHERE full_name = ?1 AND id != ?2)", nativeQuery = true)
    Long existsByFullNameNotId(String fullName, UUID id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM patients WHERE phone = ?1 AND id != ?2)", nativeQuery = true)
    Long existsByPhoneNotId(String phone, UUID id);
}
