package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.BookingDTO;
import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.entity.GasAgencyEntity;
import cfg.lms.gbs.repository.BookingRepository;
import cfg.lms.gbs.repository.CustomerRepository;
import cfg.lms.gbs.repository.GasAgencyRepository;
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

    public BookingDTO createBooking(BookingDTO dto) {
        Optional<CustomerEntity> customerOpt = customerRepo.findById(dto.getCustomerid());
        Optional<GasAgencyEntity> agencyOpt = agencyRepo.findById(dto.getAgencyid());

        if (customerOpt.isEmpty() || agencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid customer ID or agency ID");
        }
        try {
            if (bookingRepo.existsById(dto.getBookingid())) {
                throw new RuntimeException("Booking with BookingID " + dto.getBookingid() + " already exists");
            }
        BookingEntity booking = new BookingEntity();
        booking.setBookingid(dto.getBookingid());
        booking.setGasType(dto.getGasType());
//        booking.setStatus("CONFIRMED");
        booking.setBookingDate(LocalDate.now());
        booking.setCustomer(customerOpt.get());
        booking.setGasAgency(agencyOpt.get());

        BookingEntity saved = bookingRepo.save(booking);

        BookingDTO result = new BookingDTO();
        result.setBookingid(saved.getBookingid());
        result.setGasType(saved.getGasType());
        result.setStatus(saved.getStatus());
        result.setBookingDate(saved.getBookingDate().toString());
        result.setCustomerid(saved.getCustomer().getId());
        result.setAgencyid(saved.getGasAgency().getGasid());
        return result;
    }
        catch (Exception e) {
            throw new RuntimeException("Booking failed: " + e.getMessage());
        }
    }
    public BookingDTO fetchBookingById(int id) {
        Optional<BookingEntity> opt = bookingRepo.findById(id);
        if (opt.isPresent()) {
            BookingEntity b = opt.get();
            BookingDTO dto = new BookingDTO();
            dto.setBookingid(b.getBookingid());
            dto.setGasType(b.getGasType());
            dto.setStatus(b.getStatus());
            dto.setBookingDate(b.getBookingDate().toString());
            dto.setCustomerid(b.getCustomer().getId());
            dto.setAgencyid(b.getGasAgency().getGasid());
            return dto;
        } else {
            return null;
        }
    }

    public List<BookingDTO> fetchAllBookings() {
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

    public BookingDTO updateBookingStatus(int id, String status) {
        BookingEntity booking = bookingRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking ID not found"));
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
}
