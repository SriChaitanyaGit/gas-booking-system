package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.*;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    public List<CustomerDTO> fetchAllCustomers() {
        return repo.findAll().stream().map(e -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            dto.setRole(e.getRole());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<CustomerDTO> searchCustomers(String keyword) {
        return repo.findAll().stream()
            .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                         c.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                         c.getPhone().contains(keyword))
            .map(c -> {
                CustomerDTO dto = new CustomerDTO();
                dto.setId(c.getId());
                dto.setName(c.getName());
                dto.setEmail(c.getEmail());
                dto.setPhone(c.getPhone());
                dto.setRole(c.getRole());
                return dto;
            }).collect(Collectors.toList());
    }
    
    public CustomerDTO fetchCustomerById(int id) {
        return repo.findById(id).map(e -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            dto.setRole(e.getRole());
            return dto;
        }).orElse(null);
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        if (repo.existsById(dto.getId())) {
            throw new RuntimeException("Customer with ID " + dto.getId() + " already exists");
        }
        CustomerEntity entity = new CustomerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());

        CustomerEntity saved = repo.save(entity);

        CustomerDTO result = new CustomerDTO();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setEmail(saved.getEmail());
        result.setPhone(saved.getPhone());
        result.setRole(saved.getRole());
        return result;
    }

    public void deleteCustomer(int id) {
        repo.deleteById(id);
    }

    public String login(String email, String password) {
        return repo.findByEmailAndPassword(email, password)
                   .map(CustomerEntity::getRole)
                   .orElse(null);
    }

    public CustomerDTO fetchCustomerByLogin(String email, String password) {
        return repo.findByEmailAndPassword(email, password).map(e -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            dto.setRole(e.getRole());
            return dto;
        }).orElse(null);
    }

    public CustomerDTO fetchCustomerProfile(String email, String password) {
        return repo.findByEmailAndPassword(email, password).map(e -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            dto.setPassword(e.getPassword());
            dto.setRole(e.getRole());
            return dto;
        }).orElse(null);
    }

    public boolean updateCustomerProfile(CustomerDTO dto) {
        Optional<CustomerEntity> opt = repo.findByEmail(dto.getEmail());
        if (opt.isPresent()) {
            CustomerEntity entity = opt.get();
            entity.setName(dto.getName());
            entity.setPhone(dto.getPhone());
            entity.setPassword(dto.getPassword());
            entity.setRole(dto.getRole());
            repo.save(entity);
            return true;
        }
        return false;
    }

    public boolean updateCustomerPassword(String email, String newPassword) {
        Optional<CustomerEntity> opt = repo.findByEmail(email);
        if (opt.isPresent()) {
            CustomerEntity entity = opt.get();
            entity.setPassword(newPassword);
            repo.save(entity);
            return true;
        }
        return false;
    }
    
    
    public boolean updateCustomerById(int id, CustomerDTO dto) {
        Optional<CustomerEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            CustomerEntity entity = opt.get();
            entity.setName(dto.getName());
            entity.setEmail(dto.getEmail());
            entity.setPhone(dto.getPhone());
            entity.setRole(dto.getRole());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                entity.setPassword(dto.getPassword());
            }
            repo.save(entity);
            return true;
        }
        return false;
    }


}
