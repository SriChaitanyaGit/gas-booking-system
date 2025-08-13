package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.PaymentDTO;
import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.entity.PaymentEntity;
import cfg.lms.gbs.repository.BookingRepository;
import cfg.lms.gbs.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;

    public PaymentDTO createPayment(PaymentDTO dto) {
        Optional<BookingEntity> bookingOpt = bookingRepo.findById(dto.getBookingid());
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking ID not found");
        }

        BookingEntity booking = bookingOpt.get();

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentMode(dto.getPaymentMode());
        payment.setAmount(dto.getAmount());
        payment.setPaymentStatus(
                dto.getPaymentStatus() == null || dto.getPaymentStatus().trim().isEmpty()
                        ? "Pending" : dto.getPaymentStatus()
        );
        payment.setBooking(booking);

        booking.setStatus("HOLD");
        bookingRepo.save(booking);

        PaymentEntity saved = paymentRepo.save(payment);

        PaymentDTO result = new PaymentDTO();
        result.setPaymentid(saved.getPaymentid());
        result.setPaymentMode(saved.getPaymentMode());
        result.setAmount(saved.getAmount());
        result.setPaymentStatus(saved.getPaymentStatus());
        result.setBookingid(saved.getBooking().getBookingid());
        return result;
    }

    public List<PaymentDTO> fetchAllPayments() {
        return paymentRepo.findAll().stream().map(e -> {
            PaymentDTO dto = new PaymentDTO();
            dto.setPaymentid(e.getPaymentid());
            dto.setPaymentMode(e.getPaymentMode());
            dto.setAmount(e.getAmount());
            dto.setPaymentStatus(e.getPaymentStatus());
            dto.setBookingid(e.getBooking().getBookingid());
            return dto;
        }).collect(Collectors.toList());
    }

    public PaymentDTO fetchPaymentById(int id) {
        return paymentRepo.findById(id)
                .map(e -> {
                    PaymentDTO dto = new PaymentDTO();
                    dto.setPaymentid(e.getPaymentid());
                    dto.setPaymentMode(e.getPaymentMode());
                    dto.setAmount(e.getAmount());
                    dto.setPaymentStatus(e.getPaymentStatus());
                    dto.setBookingid(e.getBooking().getBookingid());
                    return dto;
                })
                .orElse(null);
    }

    public void deletePayment(int id) {
        paymentRepo.deleteById(id);
    }

    public boolean updatePaymentAndBookingStatus(int bookingId, String paymentStatus, String bookingStatus) {
        Optional<BookingEntity> bookingOpt = bookingRepo.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false;
        }

        BookingEntity booking = bookingOpt.get();
        booking.setStatus(bookingStatus);
        booking.setPaymentStatus(paymentStatus);
        bookingRepo.save(booking);

        List<PaymentEntity> payments = paymentRepo.findByBooking_Bookingid(bookingId);
        if (payments.isEmpty()) {
            return false;
        }

        for (PaymentEntity payment : payments) {
            payment.setPaymentStatus(paymentStatus);
            paymentRepo.save(payment);
        }

        return true;
    }
}
