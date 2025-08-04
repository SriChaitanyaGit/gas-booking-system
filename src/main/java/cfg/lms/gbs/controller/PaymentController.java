package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.PaymentDTO;
import cfg.lms.gbs.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/create-payment")
    public ResponseData createPayment(@RequestBody PaymentDTO dto) {
        ResponseData response = new ResponseData();
        try {
            PaymentDTO created = service.createPayment(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
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
}
