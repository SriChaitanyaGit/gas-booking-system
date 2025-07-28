package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.CustomerDTO;
import cfg.lms.gbs.entity.CustomerEntity;
import cfg.lms.gbs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            return dto;
        }).collect(Collectors.toList());
    }

    public CustomerDTO fetchCustomerById(int id) {
        Optional<CustomerEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            CustomerEntity e = opt.get();
            CustomerDTO dto = new CustomerDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            return dto;
        } else {
            return null;
        }
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        try {
            if (repo.existsById(dto.getId())) {
                throw new RuntimeException("Customer with ID " + dto.getId() + " already exists");
            }
        CustomerEntity entity = new CustomerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());

        CustomerEntity saved = repo.save(entity);

        CustomerDTO result = new CustomerDTO();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setEmail(saved.getEmail());
        result.setPhone(saved.getPhone());
        return result;
    }
        catch (Exception e) {
            throw new RuntimeException("Customer creation failed: " + e.getMessage());
        }
    }


    public void deleteCustomer(int id) {
        repo.deleteById(id);
    }
}
