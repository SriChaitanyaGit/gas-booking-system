package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.*;
import cfg.lms.gbs.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService service;

    // Admin
    @PostMapping("/create-admin")
    public ResponseData createAdmin(@RequestBody AdminDTO dto) {
        ResponseData response = new ResponseData();
        AdminDTO created = service.createAdmin(dto);
        response.setStatus("success");
        response.setData(created);
        return response;
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseData deleteAdmin(@PathVariable("id") int id) {
        service.deleteAdmin(id);
        ResponseData response = new ResponseData();
        response.setStatus("success");
        response.setMessage("Admin deleted successfully");
        return response;
    }

    // Customer
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

    // Booking
    @PutMapping("/admin/update-booking-status/{id}")
    public ResponseData updateBookingStatus(@PathVariable("id") int id, @RequestParam String status) {
        ResponseData response = new ResponseData();
        BookingDTO updated = service.updateBookingStatus(id, status);
        response.setStatus("success");
        response.setData(updated);
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

    @GetMapping("/admin/all-bookings")
    public ResponseData getAllBookings() {
        ResponseData response = new ResponseData();
        List<BookingDTO> bookings = service.getAllBookings();
        response.setStatus("success");
        response.setData(bookings);
        return response;
    }

    // Payment
    @PutMapping("/admin/update-payment/{id}")
    public ResponseData updatePayment(@PathVariable("id") int id,
                                      @RequestParam String Payment_Status,
                                      @RequestParam double amount) {
        ResponseData response = new ResponseData();
        PaymentDTO updated = service.updatePayment(id, Payment_Status, amount);
//        response.setStatus("success");
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

    // Gas Agency
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
}
