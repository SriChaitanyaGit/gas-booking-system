package cfg.lms.gbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.entity.PaymentEntity;
import jakarta.transaction.Transactional;
@Repository
@Transactional

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

	List<PaymentEntity> findByBooking_Bookingid(int bookingId);

	Optional<BookingEntity> findByBooking(BookingEntity booking);

	

}
