package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.GasAgencyDTO;
import cfg.lms.gbs.service.GasAgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GasAgencyController {

    @Autowired
    private GasAgencyService service;

    @PostMapping("/create-gas-agency")
    public ResponseData createAgency(@RequestBody GasAgencyDTO dto) {
        ResponseData response = new ResponseData();
        try {
            GasAgencyDTO created = service.createAgency(dto);
            response.setStatus("success");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/fetch-all-gas-agencies")
    public ResponseData fetchAllAgencies() {
        ResponseData response = new ResponseData();
        List<GasAgencyDTO> agencies = service.fetchAllAgencies();
        response.setStatus("success");
        response.setData(agencies);
        return response;
    }

    @GetMapping("/fetch-gas-agency/{id}")
    public ResponseData fetchAgencyById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        GasAgencyDTO dto = service.fetchAgencyById(id);
        if (dto != null) {
            response.setStatus("success");
            response.setData(dto);
        } else {
            response.setStatus("failed");
            response.setMessage("Gas agency not found");
        }
        return response;
    }

    @PutMapping("/update-gas-agency/{id}")
    public ResponseData updateAgency(@PathVariable("id") int id, @RequestBody GasAgencyDTO dto) {
        ResponseData response = new ResponseData();
        try {
            GasAgencyDTO updated = service.updateAgency(id, dto);
            response.setStatus("success");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/delete-gas-agency/{id}")
    public ResponseData deleteAgency(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            service.deleteAgency(id);
            response.setStatus("success");
            response.setMessage("Gas agency deleted successfully");
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error deleting gas agency");
        }
        return response;
    }
}
