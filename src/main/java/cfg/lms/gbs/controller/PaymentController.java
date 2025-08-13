package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.PaymentDTO;
import cfg.lms.gbs.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/create-payment")
    public ResponseData createPayment(@RequestBody PaymentDTO dto) {
        ResponseData response = new ResponseData();
        try {
            PaymentDTO created = service.createPayment(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/fetch-all-payments")
    public ResponseData fetchAllPayments() {
        ResponseData response = new ResponseData();
        List<PaymentDTO> payments = service.fetchAllPayments();
        response.setStatus("success");
        response.setData(payments);
        return response;
    }

    @GetMapping("/fetch-payment/{id}")
    public ResponseData fetchPaymentById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        PaymentDTO dto = service.fetchPaymentById(id);
        if (dto != null) {
            response.setStatus("success");
            response.setData(dto);
        } else {
            response.setStatus("failed");
            response.setMessage("Payment not found");
        }
        return response;
    }

    @DeleteMapping("/delete-payment/{id}")
    public ResponseData deletePayment(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            service.deletePayment(id);
            response.setStatus("success");
            response.setMessage("Payment deleted successfully");
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error deleting payment");
        }
        return response;
    }

    @PostMapping("/update-payment-status/{bookingId}")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable int bookingId,
            @RequestBody Map<String, String> statusMap) {
        try {
            String paymentStatus = statusMap.getOrDefault("paymentStatus", "In Progress");
            String bookingStatus = statusMap.getOrDefault("bookingStatus", "In Progress");

            boolean updated = service.updatePaymentAndBookingStatus(bookingId, paymentStatus, bookingStatus);

            if (updated) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Status updated successfully"));
            } else {
                return ResponseEntity.ok(Map.of("status", "error", "message", "Booking or Payment not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Server error"));
        }
    }
}
