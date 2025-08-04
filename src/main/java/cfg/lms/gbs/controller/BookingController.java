package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.BookingDTO;
import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.repository.BookingRepository;
import cfg.lms.gbs.service.BookingService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    @Autowired
    private BookingService service;
    @Autowired
    private BookingRepository repo;

    @PostMapping("/book-gas")
    public ResponseData bookGas(@RequestBody Map<String, Object> payload) {
        ResponseData response = new ResponseData();
        try {
            String email = (String) payload.get("email");
            String cylinderType = (String) payload.get("cylinderType");
            String deliveryLocation = (String) payload.get("deliveryLocation");
            Integer agencyId = (Integer) payload.get("agencyId");

            if (agencyId == null) {
                throw new IllegalArgumentException("Agency ID is required");
            }

            BookingDTO booked = service.bookCylinder(email, cylinderType, agencyId, deliveryLocation);
            booked.setAgencyid(agencyId); // ✅ Add this line

            response.setStatus("success");
            response.setData(booked);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/hold-booking")
    public BookingDTO holdBookingBeforePayment(@RequestBody BookingDTO dto) {
        // Set delivery date = booking date + 3 days
        dto.setDeliveryDate(LocalDate.now().plusDays(3).toString());
        return service.holdBookingBeforePayment(dto);
    }

    @PostMapping("/confirm-payment/{bookingId}")
    public BookingDTO confirmBookingPayment(@PathVariable int bookingId) {
        return service.confirmBookingPayment(bookingId);
    }

    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<ResponseData> cancelBooking(@PathVariable("id") int bookingId) {
        ResponseData response = new ResponseData();
        try {
            System.out.println("Cancel request received for ID: " + bookingId);
            
            Optional<BookingEntity> optionalBooking = repo.findById(bookingId);
            if (optionalBooking.isPresent()) {
                BookingEntity booking = optionalBooking.get();
                
                booking.setStatus("CANCELED");
                repo.save(booking);
                
                response.setStatus("success");
                response.setMessage("Booking canceled successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.setStatus("error");
                response.setMessage("Booking not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();  // ✨ Will print full stack trace in console
            response.setStatus("error");
            response.setMessage("Exception occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/customer-bookings")
    public ResponseData getCustomerBookings(@RequestParam String email) {
        ResponseData response = new ResponseData();
        try {
            List<BookingDTO> bookings = service.getBookingsByCustomer(email);
            response.setStatus("success");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching customer bookings");
        }
        return response;
    }

    @DeleteMapping("/delete-booking/{id}")
    public ResponseData deleteBooking(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            service.deleteBooking(id);
            response.setStatus("success");
            response.setMessage("Booking deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/fetch-all-bookings")
    public ResponseData fetchAllBookings() {
        ResponseData response = new ResponseData();
        try {
            List<BookingDTO> bookings = service.fetchAllBookings();
            response.setStatus("success");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching all bookings");
        }
        return response;
    }

    @GetMapping("/admin/booking-requests")
    public ResponseData fetchBookingRequestsForAdmin() {
        ResponseData response = new ResponseData();
        try {
            List<BookingDTO> bookings = service.fetchAllBookings();
            response.setStatus("success");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching bookings");
        }
        return response;
    }

    @PutMapping("/admin/approve-booking/{id}")
    public ResponseData approveBooking(@PathVariable int id) {
        ResponseData response = new ResponseData();
        try {
            BookingDTO updated = service.updateBookingStatus(id, "Approved");
            response.setStatus("success");
            response.setDeliveryStatus("Pending");

            response.setMessage("Approval successful");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Failed to approve booking");
        }
        return response;
    }

    @PostMapping("/admin/reject-booking")
    public ResponseData rejectBooking(@RequestBody Map<String, Object> payload) {
        ResponseData response = new ResponseData();
        try {
            int bookingId = (int) payload.get("bookingId");
            String reason = (String) payload.get("reason");

            BookingDTO updated = service.updateBookingStatus(bookingId, "Rejected");
            response.setStatus("success");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Failed to reject booking");
        }
        return response;
    }

    @GetMapping("/admin/search-bookings")
    public ResponseData searchBookings(@RequestParam String keyword) {
        ResponseData response = new ResponseData();
        try {
            List<BookingDTO> bookings = service.searchBookings(keyword);
            response.setStatus("success");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error searching bookings");
        }
        return response;
    }
    @PutMapping("/api/admin/update-delivery-status/{bookingId}")
    public ResponseData updateDeliveryStatus(@PathVariable int bookingId, @RequestBody Map<String, String> body) {
        ResponseData res = new ResponseData();
        Optional<BookingEntity> optionalBooking = repo.findById(bookingId);
        if (optionalBooking.isPresent()) {
            BookingEntity booking = optionalBooking.get();
            booking.setDeliveryStatus(body.get("deliveryStatus"));
            repo.save(booking);
            res.setStatus("success");
            res.setMessage("Delivery status updated.");
        } else {
            res.setStatus("fail");
            res.setMessage("Booking not found.");
        }
        return res;
    }



    @Data
    static class ResponseData {
        public void setDeliveryStatus(String string) {
			// TODO Auto-generated method stub
			
		}
		private String status;
        private String message;
        private Object data;
    }
}
