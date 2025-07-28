package cfg.lms.gbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.lms.gbs.entity.AdminEntity;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface AdminRepository extends JpaRepository<AdminEntity, Integer>{

}
