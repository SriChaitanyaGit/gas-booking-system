package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.BookingDTO;
import cfg.lms.gbs.dtos.PaymentStatusUpdateDTO;
import cfg.lms.gbs.entity.BookingEntity;
import cfg.lms.gbs.entity.GasAgencyEntity;
import cfg.lms.gbs.repository.BookingRepository;
import cfg.lms.gbs.repository.GasAgencyRepository;
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
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingService service;
    @Autowired
    private BookingRepository repo;
    @Autowired
    private GasAgencyRepository agencyRepo;

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
    public ResponseData confirmBookingPayment(@PathVariable int bookingId) {
        ResponseData response = new ResponseData();
        try {
        	Optional<BookingEntity> optionalBooking = repo.findById(bookingId);
        	if (!optionalBooking.isPresent()) {
        	    response.setStatus("failed");
        	    response.setMessage("Booking not found.");
        	    return response;
        	}
        	BookingEntity booking = optionalBooking.get(); // ✅ Only safe after check

            booking.setStatus("Paid");
            repo.save(booking);

            response.setStatus("success");
            response.setMessage("Payment confirmed and booking updated.");
            response.setData(booking);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error during payment confirmation: " + e.getMessage());
        }
        return response;
    }


    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<ResponseData> cancelBooking(@PathVariable("id") int bookingId) {
        ResponseData response = new ResponseData();
        try {
            System.out.println("Cancel request received for ID: " + bookingId);

            Optional<BookingEntity> optionalBooking = repo.findById(bookingId);
            if (optionalBooking.isPresent()) {
                BookingEntity booking = optionalBooking.get();

                // Avoid double cancellation
                if ("CANCELED".equalsIgnoreCase(booking.getStatus())) {
                    response.setStatus("error");
                    response.setMessage("Booking already canceled.");
                    return ResponseEntity.badRequest().body(response);
                }

                // Restore stock before canceling
                Optional<GasAgencyEntity> optionalAgency = agencyRepo.findById(booking.getGasAgency().getGasid());
                if (optionalAgency.isPresent()) {
                    GasAgencyEntity agency = optionalAgency.get();
                    if ("Domestic".equalsIgnoreCase(booking.getCylinderType())) {
                        agency.setDomesticCylinders(agency.getDomesticCylinders() + 1);
                    } else if ("Commercial".equalsIgnoreCase(booking.getCylinderType())) {
                        agency.setCommercialCylinders(agency.getCommercialCylinders() + 1);
                    }
                    agencyRepo.save(agency);
                }

                // Update status
                booking.setStatus("CANCELED");
                repo.save(booking);

                response.setStatus("success");
                response.setMessage("Booking canceled successfully, cylinder stock updated.");
                return ResponseEntity.ok(response);
            } else {
                response.setStatus("error");
                response.setMessage("Booking not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public ResponseData approveBooking(@PathVariable int id, @RequestBody Map<String, String> payload) {
        ResponseData response = new ResponseData();
        try {
            String bookingStatus = payload.get("bookingStatus");       // e.g. "APPROVED"
            String deliveryStatus = payload.get("deliveryStatus");     // e.g. "DELIVERED"

            // Call service method that updates both status and delivery status
            BookingDTO updated = service.updateBookingAndDeliveryStatus(id, bookingStatus, deliveryStatus);

            response.setStatus("success");
            response.setMessage("Approval successful");
            response.setData(updated);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("failed");
            response.setMessage("Failed to approve booking: " + e.getMessage());
        }
        return response;
    }


    
    
    
    @PostMapping("/confirm-booking/{bookingId}")
    public ResponseEntity<?> confirmBooking(@PathVariable int bookingId) {
        try {
            Optional<BookingEntity> bookingOpt =repo.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "Booking not found"));
            }

            BookingEntity booking = bookingOpt.get();
            booking.setStatus("Confirmed");
            repo.save(booking);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Booking confirmed successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Server error during booking confirmation"));
        }
    }

    @PostMapping("/admin/reject-booking")
    public ResponseData rejectBooking(@RequestBody Map<String, Object> payload) {
        ResponseData response = new ResponseData();
        try {
            int bookingId = (int) payload.get("bookingId");
            String reason = (String) payload.get("reason");

            BookingDTO updated = service.updateBookingStatusWithReason(bookingId, "Rejected", reason);
            response.setStatus("success");
            response.setMessage("Booking rejected successfully");
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
    
//    @PutMapping("/admin/update-delivery-status/{bookingId}")
//    public ResponseData updateDeliveryStatus(
//            @PathVariable int bookingId,
//            @RequestBody Map<String, String> body) {
//
//        String deliveryStatus = body.get("deliveryStatus");
//        ResponseData res = new ResponseData();
//
//        if (deliveryStatus == null || deliveryStatus.isBlank()) {
//            res.setStatus("failed");
//            res.setMessage("Delivery status is required.");
//            return res;
//        }
//
//        boolean updated = service.updateDeliveryStatus(bookingId, deliveryStatus);
//
//        if (updated) {
//            res.setStatus("success");
//
//            // Custom success messages for each status
//            switch (deliveryStatus.toUpperCase()) {
//                case "READY TO SHIP":
//                    res.setMessage("✅ Booking is ready to ship!");
//                    break;
//                case "SHIPMENT CREATED":
//                    res.setMessage("📦 Shipment created successfully!");
//                    break;
//                case "SHIPPED":
//                    res.setMessage("🚚 Booking shipped successfully!");
//                    break;
//                case "DELIVERED":
//                    res.setMessage("✅ Booking delivered successfully!");
//                    break;
//                default:
//                    res.setMessage("Delivery status updated.");
//            }
//        } else {
//            res.setStatus("error");
//            res.setMessage("Booking not found or status already set.");
//        }
//
//        return res;
//    }




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
