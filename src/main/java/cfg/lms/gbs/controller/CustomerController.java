package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.CustomerDTO;
import cfg.lms.gbs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/fetch-all-customers")
    public ResponseData fetchAllCustomers() {
        ResponseData response = new ResponseData();
        List<CustomerDTO> customers = service.fetchAllCustomers();
        response.setStatus("success");
        response.setData(customers);
        return response;
    }

    @GetMapping("/fetch-customer/{id}")
    public ResponseData fetchCustomerById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        CustomerDTO customer = service.fetchCustomerById(id);

        if (customer != null) {
            response.setStatus("success");
            response.setData(customer);
        } else {
            response.setStatus("failed");
            response.setMessage("Customer not found");
        }
        return response;
    }

    @PostMapping("/create-customer")
    public ResponseData createCustomer(@RequestBody CustomerDTO dto) {
        ResponseData response = new ResponseData();
        try {
        CustomerDTO created = service.createCustomer(dto);
        response.setStatus("success");
        response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/delete-customer/{id}")
    public ResponseData deleteCustomer(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            service.deleteCustomer(id);
            response.setStatus("success");
            response.setMessage("Customer deleted successfully");
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error deleting customer");
        }
        return response;
    }
}
