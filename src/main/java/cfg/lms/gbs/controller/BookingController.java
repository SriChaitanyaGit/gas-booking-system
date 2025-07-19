package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.BookingDTO;
import cfg.lms.gbs.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    private BookingService service;

    @PostMapping("/create-booking")
    public ResponseData createBooking(@RequestBody BookingDTO dto) {
        ResponseData response = new ResponseData();
        try {
            BookingDTO created = service.createBooking(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/fetch-all-bookings")
    public ResponseData fetchAllBookings() {
        ResponseData response = new ResponseData();
        List<BookingDTO> bookings = service.fetchAllBookings();
        response.setStatus("success");
        response.setData(bookings);
        return response;
    }

    @GetMapping("/fetch-booking/{id}")
    public ResponseData fetchBookingById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        BookingDTO booking = service.fetchBookingById(id);
        if (booking != null) {
            response.setStatus("success");
            response.setData(booking);
        } else {
            response.setStatus("failed");
            response.setMessage("Booking not found");
        }
        return response;
    }

    @PutMapping("/update-booking-status/{id}")
    public ResponseData updateBookingStatus(@PathVariable("id") int id, @RequestParam String status) {
        ResponseData response = new ResponseData();
        try {
            BookingDTO updated = service.updateBookingStatus(id, status);
            response.setStatus("success");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
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
            response.setStatus("failed");
            response.setMessage("Error deleting booking");
        }
        return response;
    }
}
