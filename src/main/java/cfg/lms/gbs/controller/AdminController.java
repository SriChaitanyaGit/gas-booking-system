package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.*;
import cfg.lms.gbs.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService service;

    // Admin Endpoints
    @PostMapping("/create-admin")
    public ResponseData createAdmin(@RequestBody AdminDTO dto) {
        ResponseData response = new ResponseData();
        AdminDTO created = service.createAdmin(dto);
        response.setStatus("success");
        response.setData(created);
        return response;
    }

    @PostMapping("/login-admin")
    public ResponseEntity<ResponseData> loginAdmin(@RequestBody AdminDTO dto) {
        ResponseData response = new ResponseData();
        try {
            AdminDTO loggedIn = service.login(dto.getEmail(), dto.getPassword());
            response.setStatus("success");
            response.setData(loggedIn);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Invalid login credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseData deleteAdmin(@PathVariable("id") int id) {
        service.deleteAdmin(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Admin deleted successfully");
        return response;
    }

    // Customer Endpoints
    @PostMapping("/admin/add-customer")
    public ResponseData addCustomer(@RequestBody CustomerDTO dto) {
        ResponseData response = new ResponseData();
        try {
            CustomerDTO created = service.addCustomer(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/admin/delete-customer/{id}")
    public ResponseData deleteCustomer(@PathVariable("id") int id) {
        service.deleteCustomer(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Customer deleted successfully");
        return response;
    }

    @GetMapping("/admin/all-customers")
    public ResponseData getAllCustomers() {
        ResponseData response = new ResponseData();
        List<CustomerDTO> customers = service.getAllCustomers();
        response.setStatus("success");
        response.setData(customers);
        return response;
    }

    // Booking Endpoints
    @GetMapping("/admin/all-bookings")
    public ResponseData getAllBookings() {
        ResponseData response = new ResponseData();
        List<BookingDTO> bookings = service.getAllBookings();
        response.setStatus("success");
        response.setData(bookings);
        return response;
    }

    @DeleteMapping("/admin/delete-booking/{id}")
    public ResponseData deleteBooking(@PathVariable("id") int id) {
        service.deleteBooking(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Booking deleted successfully");
        return response;
    }

    /**
     * Approve or reject a booking payment with optional reject reason.
     * Example usage:
     * PUT /api/admin/update-booking-payment/123?action=approve
     * PUT /api/admin/update-booking-payment/123?action=reject&rejectReason=Out of stock
     */
    @PutMapping("/admin/update-booking-payment/{id}")
    public ResponseEntity<ResponseData> updateBookingPaymentStatus(
            @PathVariable("id") int bookingId,
            @RequestParam String action,
            @RequestParam(required = false) String rejectReason) {

        ResponseData response = new ResponseData();

        try {
            BookingDTO updated = service.updateBookingPaymentStatus(bookingId, action, rejectReason);
            response.setStatus("success");
            response.setData(updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Payment Endpoints
    @PutMapping("/admin/update-payment/{id}")
    public ResponseData updatePayment(@PathVariable("id") int id,
                                      @RequestParam String Payment_Status,
                                      @RequestParam double amount) {
        ResponseData response = new ResponseData();
        PaymentDTO updated = service.updatePayment(id, Payment_Status, amount);
        response.setStatus("success");
        response.setData(updated);
        return response;
    }

    @DeleteMapping("/admin/delete-payment/{id}")
    public ResponseData deletePayment(@PathVariable("id") int id) {
        service.deletePayment(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Payment deleted successfully");
        return response;
    }

    @GetMapping("/admin/all-payments")
    public ResponseData getAllPayments() {
        ResponseData response = new ResponseData();
        List<PaymentDTO> payments = service.getAllPayments();
        response.setStatus("success");
        response.setData(payments);
        return response;
    }

    // Gas Agency Endpoints
    @PutMapping("/admin/update-gas-agency/{id}")
    public ResponseData updateGasAgency(@PathVariable("id") int id,
                                        @RequestParam String name,
                                        @RequestParam String location) {
        ResponseData response = new ResponseData();
        GasAgencyDTO updated = service.updateAgency(id, name, location);
        response.setStatus("success");
        response.setData(updated);
        return response;
    }

    @DeleteMapping("/admin/delete-gas-agency/{id}")
    public ResponseData deleteGasAgency(@PathVariable("id") int id) {
        service.deleteAgency(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Gas agency deleted successfully");
        return response;
    }

    @GetMapping("/admin/all-gas-agencies")
    public ResponseData getAllGasAgencies() {
        ResponseData response = new ResponseData();
        List<GasAgencyDTO> agencies = service.getAllAgencies();
        response.setStatus("success");
        response.setData(agencies);
        return response;
    }

    @PostMapping("/admin/add-gas-agency")
    public ResponseData addGasAgency(@RequestBody GasAgencyDTO dto) {
        ResponseData response = new ResponseData();
        try {
            GasAgencyDTO created = service.addGasAgency(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Book Gas for customer
    @PostMapping("/admin/book-gas")
    public ResponseData adminBookGas(@RequestParam int customerId,
                                     @RequestParam int agencyId,
                                     @RequestParam String cylinderType) {
        ResponseData response = new ResponseData();
        try {
            BookingDTO booked = service.bookGasForCustomer(customerId, agencyId, cylinderType);
            response.setStatus("success");
            response.setData(booked);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Delivery Status Update
    @PutMapping("/admin/update-delivery-status/{bookingId}")
    public ResponseData updateDeliveryStatus(
            @PathVariable int bookingId,
            @RequestBody Map<String, String> body) {

        String deliveryStatus = body.get("deliveryStatus");
        ResponseData res = new ResponseData();

        if (deliveryStatus == null || deliveryStatus.isBlank()) {
            res.setStatus("failed");
            res.setMessage("Delivery status is required.");
            return res;
        }

        boolean updated = service.updateDeliveryStatus(bookingId, deliveryStatus);

        if (updated) {
            res.setStatus("success");
            switch (deliveryStatus.toUpperCase()) {
                case "READY TO SHIP":
                    res.setMessage("✅ Booking is ready to ship!");
                    break;
                case "SHIPMENT CREATED":
                    res.setMessage("📦 Shipment created successfully!");
                    break;
                case "SHIPPED":
                    res.setMessage("🚚 Booking shipped successfully!");
                    break;
                case "DELIVERED":
                    res.setMessage("✅ Booking delivered successfully!");
                    break;
                default:
                    res.setMessage("Delivery status updated.");
            }
        } else {
            res.setStatus("error");
            res.setMessage("Booking not found or status already set.");
        }

        return res;
    }
    
    

}
