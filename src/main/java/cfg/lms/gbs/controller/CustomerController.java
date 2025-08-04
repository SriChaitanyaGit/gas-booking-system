package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.*;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.repository.CustomerRepository;
import cfg.lms.gbs.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // for Angular
public class CustomerController {

    @Autowired
    private CustomerService service;
    @Autowired
    private CustomerRepository Repo;

    @GetMapping("/fetch-all-customers")
    public ResponseData fetchAllCustomers() {
        ResponseData response = new ResponseData();
        List<CustomerDTO> customers = service.fetchAllCustomers();
        response.setStatus("success");
        response.setData(customers);
        return response;
    }

    @GetMapping("/fetch-customer/{id}")
    public ResponseData fetchCustomerById(@PathVariable int id) {
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

        if (Repo.existsByEmail(dto.getEmail())) {
            response.setStatus("fail");
            response.setMessage("Email already registered.");
            return response;
        }

        Repo.save(mapToEntity(dto));
        response.setStatus("success");
        response.setMessage("Registration successful!");
        return response;
    }
    private CustomerEntity mapToEntity(CustomerDTO dto) {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
//        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setPassword(dto.getPassword());
        customer.setRole(dto.getRole());
        return customer;
    }


    @DeleteMapping("/delete-customer/{id}")
    public ResponseData deleteCustomer(@PathVariable int id) {
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

    @PostMapping("/login-customer")
    public ResponseData loginCustomer(@RequestBody LoginRequest request) {
        ResponseData response = new ResponseData();
        String role = service.login(request.getEmail(), request.getPassword());
        if (role != null) {
            response.setStatus("success");
            response.setData(role);
        } else {
            response.setStatus("failed");
            response.setMessage("Invalid email or password");
        }
        return response;
    }

    @PostMapping("/customer-profile")
    public ResponseData getCustomerProfile(@RequestBody LoginRequest request) {
        ResponseData response = new ResponseData();
        CustomerDTO customer = service.fetchCustomerProfile(request.getEmail(), request.getPassword());

        if (customer != null) {
            response.setStatus("success");
            response.setData(customer); // Includes name, email, phone, role
        } else {
            response.setStatus("failed");
            response.setMessage("Invalid credentials");
        }

        return response;
    }

    @PutMapping("/customer-profile/update")
    public ResponseData updateCustomerProfile(@RequestBody CustomerDTO dto) {
        ResponseData response = new ResponseData();
        boolean success = service.updateCustomerProfile(dto);

        if (success) {
            response.setStatus("success");
            response.setMessage("Profile updated successfully");
        } else {
            response.setStatus("failed");
            response.setMessage("Update failed: customer not found");
        }

        return response;
    }

    @PutMapping("/update-password")
    public ResponseData updateCustomerPassword(@RequestBody PasswordUpdateRequest request) {
        ResponseData response = new ResponseData();
        boolean success = service.updateCustomerPassword(request.getEmail(), request.getNewPassword());

        if (success) {
            response.setStatus("success");
            response.setMessage("Password updated successfully");
        } else {
            response.setStatus("failed");
            response.setMessage("Password update failed: customer not found");
        }

        return response;
    }@GetMapping("/search-customers")
    public ResponseData searchCustomers(@RequestParam String keyword) {
        ResponseData response = new ResponseData();
        List<CustomerDTO> customers = service.searchCustomers(keyword);
        response.setStatus("success");
        response.setData(customers);
        return response;
    }

    @PutMapping("/update-customer/{id}")
    public ResponseData updateCustomerById(@PathVariable int id, @RequestBody CustomerDTO dto) {
        ResponseData response = new ResponseData();
        boolean success = service.updateCustomerById(id, dto);
        if (success) {
            response.setStatus("success");
            response.setMessage("Customer updated successfully");
        } else {
            response.setStatus("failed");
            response.setMessage("Update failed: customer not found");
        }
        return response;
    }
    
    

}
