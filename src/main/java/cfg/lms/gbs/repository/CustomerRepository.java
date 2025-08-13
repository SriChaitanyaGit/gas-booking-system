package cfg.lms.gbs.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cfg.lms.gbs.entity.CustomerEntity;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByEmailAndPassword(String email, String password);

	Optional<CustomerEntity> findByEmail(String email);
	boolean existsByEmail(String email);

}
