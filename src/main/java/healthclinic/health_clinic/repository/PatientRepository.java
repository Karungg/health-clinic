package healthclinic.health_clinic.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByFullNameEquals(String fullName);

    Optional<Patient> findByNikEquals(String nik);

    Optional<Patient> findByPhoneEquals(String phone);

    boolean existsByNikAndIdNot(String nik, UUID id);

    boolean existsByPhoneAndIdNot(String phone, UUID id);
}
