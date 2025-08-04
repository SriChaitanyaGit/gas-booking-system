package cfg.lms.gbs.repository;

import cfg.lms.gbs.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    List<BookingEntity> findByCustomer_Email(String email);
    List<BookingEntity> findByGasAgency_Gasid(int gasid);
    Optional<BookingEntity> findByCustomer_IdAndCylinderType(int customerid, String cylinderType);
}
