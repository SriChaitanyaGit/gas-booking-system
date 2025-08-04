package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.GasAgencyDTO;
import cfg.lms.gbs.entity.GasAgencyEntity;
import cfg.lms.gbs.service.GasAgencyService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class GasAgencyController {

    @Autowired
    private GasAgencyService service;

    // ✅ Create new agency
    @PostMapping("/gas-agency")
    public ResponseData createAgency(@RequestBody GasAgencyDTO dto) {
        ResponseData response = new ResponseData();
        try {
            GasAgencyDTO created = service.createAgency(dto);
            response.setStatus("success");
            response.setMessage("✅ Gas agency created successfully!");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // ✅ Fetch all agencies
    @GetMapping("/gas-agencies")
    public ResponseData fetchAllAgencies() {
        ResponseData response = new ResponseData();
        try {
            List<GasAgencyDTO> agencies = service.fetchAllAgencies();
            response.setStatus("success");
            response.setData(agencies);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching gas agencies");
        }
        return response;
    }

    // ✅ Fetch agency by ID
    @GetMapping("/gas-agency/{id}")
    public ResponseData fetchAgencyById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            GasAgencyDTO dto = service.fetchAgencyById(id);
            if (dto != null) {
                response.setStatus("success");
                response.setData(dto);
            } else {
                response.setStatus("failed");
                response.setMessage("Gas agency not found");
            }
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching gas agency");
        }
        return response;
    }

    // ✅ Update agency
    @PutMapping("/update-gas-agency/{id}")
    public Map<String, Object> updateGasAgency(@PathVariable Integer id, @RequestBody GasAgencyDTO agencyDto) {
        Map<String, Object> res = new HashMap<>();
        boolean updated = service.updateGasAgency(id, agencyDto);

        if (updated) {
            res.put("status", "success");
            res.put("message", "✅ Gas agency updated successfully!");
        } else {
            res.put("status", "error");
            res.put("message", "Gas agency not found");
        }
        return res;
    }

    // ✅ Delete agency
    @DeleteMapping("/delete-gas-agency/{id}")
    public Map<String, Object> deleteGasAgency(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        if (service.deleteGasAgency(id)) {
            res.put("status", "success");
            res.put("message", "✅ Gas agency deleted successfully!");
        } else {
            res.put("status", "error");
            res.put("message", "Gas agency not found");
        }
        return res;
    }

    @Data
    static class ResponseData {
        private String status;
        private String message;
        private Object data;
    }
}
