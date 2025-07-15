package healthclinic.health_clinic.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import healthclinic.health_clinic.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByIdEquals(UUID id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?1 AND id != ?2)", nativeQuery = true)
    Long existsByUsernameNotId(String username, UUID id);
}
