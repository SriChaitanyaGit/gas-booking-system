package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.PaymentStatusUpdateDTO;
import cfg.lms.gbs.dtos.BookingDTO;
import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.entity.GasAgencyEntity;
import cfg.lms.gbs.entity.PaymentEntity;
import cfg.lms.gbs.repository.BookingRepository;
import cfg.lms.gbs.repository.CustomerRepository;
import cfg.lms.gbs.repository.GasAgencyRepository;
import cfg.lms.gbs.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final GasAgencyRepository agencyRepo;
    private final PaymentRepository paymentRepo;

    public BookingDTO createBooking(BookingDTO dto) {
        Optional<CustomerEntity> customerOpt = customerRepo.findById(dto.getCustomerid());
        Optional<GasAgencyEntity> agencyOpt = agencyRepo.findById(dto.getAgencyid());

        if (customerOpt.isEmpty() || agencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid customer ID or agency ID");
        }

        BookingEntity booking = new BookingEntity();
        booking.setCustomer(customerOpt.get());
        booking.setGasAgency(agencyOpt.get());
        booking.setCylinderType(dto.getCylinderType());
        booking.setStatus(dto.getStatus() != null ? dto.getStatus() : "In Progress");
        booking.setBookingDate(LocalDate.now());
        booking.setDeliveryDate(LocalDate.now().plusDays(3)); // ✅ Set delivery date
        booking.setDeliveryLocation(dto.getDeliveryLocation());

        BookingEntity saved = bookingRepo.save(booking);
        return mapToDTO(saved);
    }

    public BookingDTO bookCylinder(String email, String cylinderType, Integer agencyId, String deliveryLocation) {
        CustomerEntity customer = customerRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        GasAgencyEntity agency = agencyRepo.findById(agencyId)
            .orElseThrow(() -> new RuntimeException("Agency not found"));

        if ("Domestic".equalsIgnoreCase(cylinderType)) {
            if (agency.getDomesticCylinders() <= 0) {
                throw new RuntimeException("No domestic cylinders available");
            }
            agency.setDomesticCylinders(agency.getDomesticCylinders() - 1);
        } else if ("Commercial".equalsIgnoreCase(cylinderType)) {
            if (agency.getCommercialCylinders() <= 0) {
                throw new RuntimeException("No commercial cylinders available");
            }
            agency.setCommercialCylinders(agency.getCommercialCylinders() - 1);
        }
        agencyRepo.save(agency);

        BookingEntity booking = new BookingEntity();
        booking.setCustomer(customer);
        booking.setGasAgency(agency);
        booking.setCylinderType(cylinderType);
        booking.setDeliveryLocation(deliveryLocation);
        booking.setStatus("In Progress");
        booking.setBookingDate(LocalDate.now());
        booking.setDeliveryDate(LocalDate.now().plusDays(3)); // ✅ Set delivery date
        bookingRepo.save(booking);

        return mapToDTO(booking);
    }

    public List<BookingDTO> fetchAllBookings() {
        return bookingRepo.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByCustomer(String email) {
        return bookingRepo.findByCustomer_Email(email).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByAgency(int gasid) {
        return bookingRepo.findByGasAgency_Gasid(gasid).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void updatePaymentAndBookingStatus(int bookingId, PaymentStatusUpdateDTO dto) {
        BookingEntity booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Update booking
        booking.setPaymentStatus(dto.getPaymentStatus());
        booking.setStatus(dto.getBookingStatus());
        bookingRepo.save(booking);

        // Also update payment when booking is approved
        PaymentEntity payment = (PaymentEntity) paymentRepo.findByBooking_Bookingid((int) bookingId);
               

        if ("APPROVED".equalsIgnoreCase(dto.getBookingStatus())) {
            payment.setPaymentStatus("RECEIVED"); // mark payment as received
        } else {
            // Keep payment in sync with booking payment status
            payment.setPaymentStatus(dto.getPaymentStatus());
        }

        paymentRepo.save(payment);
    }

    
    

    public BookingDTO updateBookingStatus(int id, String status) {
        BookingEntity booking = bookingRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking ID not found"));
        
        booking.setStatus(status);
        if ("Approved".equalsIgnoreCase(status)) {
            booking.setRejectionReason(null);
        }
        
        BookingEntity updated = bookingRepo.save(booking);
        return mapToDTO(updated);
    }


    public BookingDTO updateBookingByCustomerAndType(int customerid, String cylinderType, String status, LocalDate deliveryDate) {
        BookingEntity booking = bookingRepo.findByCustomer_IdAndCylinderType(customerid, cylinderType)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(status);
        booking.setDeliveryDate(deliveryDate); // ✅ Set delivery date if passed
        return mapToDTO(bookingRepo.save(booking));
    }

    public void deleteBooking(int id) {
        if (!bookingRepo.existsById(id)) {
            throw new IllegalArgumentException("Booking ID " + id + " not found");
        }
        bookingRepo.deleteById(id);
    }

    public List<BookingDTO> searchBookings(String keyword) {
        return bookingRepo.findAll().stream()
            .filter(b -> b.getCustomer().getName().toLowerCase().contains(keyword.toLowerCase()) ||
                         b.getCustomer().getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                         b.getCustomer().getPhone().toLowerCase().contains(keyword.toLowerCase()))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public BookingDTO holdBookingBeforePayment(BookingDTO dto) {
        Optional<CustomerEntity> customerOpt = customerRepo.findByEmail(dto.getCustomerEmail());
        Optional<GasAgencyEntity> agencyOpt = agencyRepo.findById(dto.getAgencyid());

        if (customerOpt.isEmpty() || agencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid customer or agency");
        }

        BookingEntity booking = new BookingEntity();
        booking.setCustomer(customerOpt.get());
        booking.setGasAgency(agencyOpt.get());
        booking.setCylinderType(dto.getCylinderType());
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("Hold");
        booking.setDeliveryLocation(dto.getDeliveryLocation());
        booking.setDeliveryDate(LocalDate.now().plusDays(3)); // ✅ Set delivery date

        BookingEntity saved = bookingRepo.save(booking);
        return mapToDTO(saved);
    }

    public BookingDTO confirmBookingPayment(int bookingId) {
        BookingEntity booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("In Progress");
        return mapToDTO(bookingRepo.save(booking));
    }

    @Transactional
    public boolean cancelBooking(int bookingId) {
        Optional<BookingEntity> opt = bookingRepo.findById(bookingId);
        if (opt.isPresent()) {
            BookingEntity booking = opt.get();
            if (!"Canceled".equalsIgnoreCase(booking.getStatus())) {
                booking.setStatus("Canceled");
                bookingRepo.save(booking);

                // Restore cylinder stock
                GasAgencyEntity agency = agencyRepo.findById(booking.getGasAgency().getGasid())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));

                if ("Domestic".equalsIgnoreCase(booking.getCylinderType())) {
                    agency.setDomesticCylinders(agency.getDomesticCylinders() + 1);
                } else if ("Commercial".equalsIgnoreCase(booking.getCylinderType())) {
                    agency.setCommercialCylinders(agency.getCommercialCylinders() + 1);
                }
                agencyRepo.save(agency);

                return true;
            }
        }
        return false;
    }

    
    @Transactional
    public BookingDTO updateBookingStatusWithReason(int id, String status, String rejectionReason) {
        BookingEntity booking = bookingRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking ID not found"));
        
        booking.setStatus(status);
        
        if ("Rejected".equalsIgnoreCase(status)) {
            booking.setRejectionReason(rejectionReason);
        } else {
            booking.setRejectionReason(null); // Clear rejection reason if not rejected
        }
        
        BookingEntity updated = bookingRepo.save(booking);
        return mapToDTO(updated);
    }


    private BookingDTO mapToDTO(BookingEntity b) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingid(b.getBookingid());
        dto.setCustomerid(b.getCustomer().getId());
        dto.setCustomerName(b.getCustomer().getName());
        dto.setCustomerEmail(b.getCustomer().getEmail());
        dto.setCustomerPhone(b.getCustomer().getPhone());
        dto.setAgencyid(b.getGasAgency().getGasid());
        dto.setAgencyName(b.getGasAgency().getName());
        dto.setCylinderType(b.getCylinderType());
        dto.setStatus(b.getStatus());
        dto.setRejectionReason(b.getRejectionReason());  // Added
        dto.setBookingDate(b.getBookingDate().toString());
        dto.setDeliveryDate(b.getDeliveryDate() != null ? b.getDeliveryDate().toString() : null);
        dto.setDeliveryLocation(b.getDeliveryLocation());
        dto.setDeliveryStatus(b.getDeliveryStatus());  // Added
        dto.setPaymentStatus(b.getPaymentStatus());    // Added if needed
        return dto;
    }
    
    @Transactional
    public BookingDTO updateBookingAndDeliveryStatus(int id, String bookingStatus, String deliveryStatus) {
        BookingEntity booking = bookingRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking ID not found"));

        booking.setStatus(bookingStatus);
        booking.setDeliveryStatus(deliveryStatus);

        // Clear rejection reason if approved
        if ("APPROVED".equalsIgnoreCase(bookingStatus)) {
            booking.setRejectionReason(null);
        }

        BookingEntity updated = bookingRepo.save(booking);
        return mapToDTO(updated);
    }

	public boolean updateDeliveryStatus(int bookingId, String deliveryStatus) {
        Optional<BookingEntity> optionalBooking = bookingRepo.findById(bookingId);

        if (optionalBooking.isPresent()) {
            BookingEntity booking = optionalBooking.get();

            // Prevent updating if status is already same
            if (deliveryStatus.equalsIgnoreCase(booking.getDeliveryStatus())) {
                return false;
            }

            booking.setDeliveryStatus(deliveryStatus);
            bookingRepo.save(booking);
            return true;
        }

        return false; // Booking not found
    }


}
