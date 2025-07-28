package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.*;
import cfg.lms.gbs.entity.*;
import cfg.lms.gbs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepo;
    private final CustomerRepository customerRepo;
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final GasAgencyRepository agencyRepo;

    // Admin
    public AdminDTO createAdmin(AdminDTO dto) {
        AdminEntity entity = new AdminEntity();
        entity.setAdminid(dto.getAdminid());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());

        AdminEntity saved = adminRepo.save(entity);
        dto.setAdminid(saved.getAdminid());
        return dto;
    }

    public void deleteAdmin(int id) {
        adminRepo.deleteById(id);
    }

    // Customer
    public CustomerDTO addCustomer(CustomerDTO dto) {
        try {
            if (customerRepo.existsById(dto.getId())) {
                throw new RuntimeException("Customer with ID " + dto.getId() + " already exists");
            }

            CustomerEntity entity = new CustomerEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setEmail(dto.getEmail());
            entity.setPhone(dto.getPhone());

            CustomerEntity saved = customerRepo.save(entity);
            dto.setId(saved.getId());
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Customer creation failed: " + e.getMessage());
        }
    }

    public void deleteCustomer(int id) {
        customerRepo.deleteById(id);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepo.findAll().stream().map(e -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            return dto;
        }).collect(Collectors.toList());
    }

    // Booking
    public BookingDTO updateBookingStatus(int bookingId, String status) {
        BookingEntity booking = bookingRepo.findById(bookingId).orElseThrow();
        booking.setStatus(status);
        BookingEntity updated = bookingRepo.save(booking);

        BookingDTO dto = new BookingDTO();
        dto.setBookingid(updated.getBookingid());
        dto.setGasType(updated.getGasType());
        dto.setStatus(updated.getStatus());
        dto.setBookingDate(updated.getBookingDate().toString());
        dto.setCustomerid(updated.getCustomer().getId());
        dto.setAgencyid(updated.getGasAgency().getGasid());
        return dto;
    }

    public void deleteBooking(int id) {
        bookingRepo.deleteById(id);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepo.findAll().stream().map(b -> {
            BookingDTO dto = new BookingDTO();
            dto.setBookingid(b.getBookingid());
            dto.setGasType(b.getGasType());
            dto.setStatus(b.getStatus());
            dto.setBookingDate(b.getBookingDate().toString());
            dto.setCustomerid(b.getCustomer().getId());
            dto.setAgencyid(b.getGasAgency().getGasid());
            return dto;
        }).collect(Collectors.toList());
    }

    // Payment
    public PaymentDTO updatePayment(int paymentId, String status, double amount) {
        PaymentEntity payment = paymentRepo.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment ID not found"));

        BookingEntity booking = payment.getBooking();

        payment.setPaymentStatus(status);
        payment.setAmount(amount);

        // Reflect payment status on booking
        booking.setStatus(
            switch (status.toUpperCase()) {
                case "APPROVED" -> "CONFIRMED";
                case "REJECTED" -> "FAILED";
                case "PENDING"  -> "PROCESSING";
                default         -> booking.getStatus();
            }
        );

        bookingRepo.save(booking);
        PaymentEntity updated = paymentRepo.save(payment);

        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentid(updated.getPaymentid());
        dto.setPaymentMode(updated.getPaymentMode());
        dto.setAmount(updated.getAmount());
        dto.setPaymentStatus(updated.getPaymentStatus());
        dto.setBookingid(updated.getBooking().getBookingid());
        return dto;
    }


    public void deletePayment(int id) {
        paymentRepo.deleteById(id);
    }

    public List<PaymentDTO> getAllPayments() {
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

    // Gas Agency
    public GasAgencyDTO updateAgency(int agencyId, String name, String location) {
        GasAgencyEntity agency = agencyRepo.findById(agencyId).orElseThrow();
        agency.setName(name);
        agency.setLocation(location);

        GasAgencyEntity updated = agencyRepo.save(agency);

        GasAgencyDTO dto = new GasAgencyDTO();
        dto.setGasid(updated.getGasid());
        dto.setName(updated.getName());
        dto.setLocation(updated.getLocation());
        return dto;
    }

    public void deleteAgency(int id) {
        agencyRepo.deleteById(id);
    }

    public List<GasAgencyDTO> getAllAgencies() {
        return agencyRepo.findAll().stream().map(e -> {
            GasAgencyDTO dto = new GasAgencyDTO();
            dto.setGasid(e.getGasid());
            dto.setName(e.getName());
            dto.setLocation(e.getLocation());
            return dto;
        }).collect(Collectors.toList());
    }
}
