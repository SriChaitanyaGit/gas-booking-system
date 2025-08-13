package cfg.lms.gbs.repository;

import cfg.lms.gbs.entity.GasAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface GasAgencyRepository extends JpaRepository<GasAgencyEntity, Integer> {
}
