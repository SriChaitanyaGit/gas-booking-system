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
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setRole("admin"); // Hardcoded for safety

        AdminEntity saved = adminRepo.save(entity);
        dto.setAdminid(saved.getAdminid());
        return dto;
    }
    
    public AdminDTO login(String email, String password) {
        AdminEntity entity = adminRepo.findByEmailAndPassword(email, password);
        if (entity != null && "admin".equalsIgnoreCase(entity.getRole())) {
            AdminDTO dto = new AdminDTO();
            dto.setAdminid(entity.getAdminid());
            dto.setUsername(entity.getUsername());
            dto.setEmail(entity.getEmail());
            dto.setAddress(entity.getAddress());
            dto.setPhone(entity.getPhone());
            dto.setPassword(entity.getPassword());
            dto.setRole(entity.getRole());
            return dto;
        }
        throw new RuntimeException("Invalid credentials or role");
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

   

    public void deleteBooking(int id) {
        bookingRepo.deleteById(id);
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
            dto.setDomesticCylinders(e.getDomesticCylinders());
            dto.setCommercialCylinders(e.getCommercialCylinders());
            return dto;
        }).collect(Collectors.toList());
    }

	public List<BookingDTO> getAllBookings() {
		// TODO Auto-generated method stub
		return null;
	}

	public BookingDTO updateBookingStatus(int id, String status) {
		// TODO Auto-generated method stub
		return null;
	}
	// Add new gas agency
	public GasAgencyDTO addGasAgency(GasAgencyDTO dto) {
	    GasAgencyEntity entity = new GasAgencyEntity();
	    entity.setName(dto.getName());
	    entity.setLocation(dto.getLocation());
	    entity.setDomesticCylinders(dto.getDomesticCylinders());
	    entity.setCommercialCylinders(dto.getCommercialCylinders());
	    GasAgencyEntity saved = agencyRepo.save(entity);
	    GasAgencyDTO response = new GasAgencyDTO();
	    response.setGasid(saved.getGasid());
	    response.setName(saved.getName());
	    response.setLocation(saved.getLocation());
	    response.setDomesticCylinders(saved.getDomesticCylinders());
	    response.setCommercialCylinders(saved.getCommercialCylinders());
	    return response;
	}
	public boolean updateDeliveryStatus(int bookingId, String deliveryStatus) {
        Optional<BookingEntity> optionalBooking = bookingRepo.findById(bookingId);

        if (optionalBooking.isPresent()) {
            BookingEntity booking = optionalBooking.get();

            // If status is already same, no need to update
            if (deliveryStatus.equalsIgnoreCase(booking.getDeliveryStatus())) {
                return false;
            }

            booking.setDeliveryStatus(deliveryStatus);
            bookingRepo.save(booking);
            return true;
        }

        return false; // Booking not found
    }


	// Book gas for a customer
	public BookingDTO bookGasForCustomer(int customerId, int agencyId, String cylinderType) {
	    CustomerEntity customer = customerRepo.findById(customerId)
	        .orElseThrow(() -> new RuntimeException("Customer not found"));
	    GasAgencyEntity agency = agencyRepo.findById(agencyId)
	        .orElseThrow(() -> new RuntimeException("Agency not found"));

	    if (cylinderType.equalsIgnoreCase("Domestic") && agency.getDomesticCylinders() <= 0) {
	        throw new RuntimeException("No domestic cylinders available");
	    }
	    if (cylinderType.equalsIgnoreCase("Commercial") && agency.getCommercialCylinders() <= 0) {
	        throw new RuntimeException("No commercial cylinders available");
	    }

	    BookingEntity booking = new BookingEntity();
	    booking.setCustomer(customer);
	    booking.setGasAgency(agency);
	    booking.setCylinderType(cylinderType);
	    booking.setStatus("Confirmed");
	    booking.setBookingDate(java.time.LocalDate.now());

	    if (cylinderType.equalsIgnoreCase("Domestic")) {
	        agency.setDomesticCylinders(agency.getDomesticCylinders() - 1);
	    } else {
	        agency.setCommercialCylinders(agency.getCommercialCylinders() - 1);
	    }

	    agencyRepo.save(agency);
	    BookingEntity saved = bookingRepo.save(booking);

	    BookingDTO dto = new BookingDTO();
	    dto.setBookingid(saved.getBookingid());
	    dto.setCustomerid(saved.getCustomer().getId());
	    dto.setAgencyid(saved.getGasAgency().getGasid());
	    dto.setCylinderType(saved.getCylinderType());
	    dto.setStatus(saved.getStatus());
	    dto.setBookingDate(saved.getBookingDate().toString());
	    return dto;
	}

	public BookingDTO updateBookingPaymentStatus(int bookingId, String action, String rejectReason) {
		// TODO Auto-generated method stub
		return null;
	}

}
