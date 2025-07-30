package healthclinic.health_clinic.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findBySipEquals(String sip);

    Optional<Doctor> findByPhoneEquals(String phone);

    boolean existsBySipAndIdNot(String sip, UUID doctorId);

    boolean existsByPhoneAndIdNot(String phone, UUID doctorId);
}
