package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.GasAgencyDTO;
import cfg.lms.gbs.entity.GasAgencyEntity;
import cfg.lms.gbs.repository.GasAgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GasAgencyService {

    private final GasAgencyRepository repo;

    // ✅ Create agency
    public GasAgencyDTO createAgency(GasAgencyDTO dto) {
        GasAgencyEntity entity = mapToEntity(dto);
        GasAgencyEntity saved = repo.save(entity);
        return mapToDTO(saved);
    }

    // ✅ Fetch all agencies
    public List<GasAgencyDTO> fetchAllAgencies() {
        return repo.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    // ✅ Fetch by ID
    public GasAgencyDTO fetchAgencyById(int id) {
        Optional<GasAgencyEntity> opt = repo.findById(id);
        return opt.map(this::mapToDTO).orElse(null);
    }

    // ✅ Update agency
    public boolean updateGasAgency(Integer id, GasAgencyDTO updatedAgency) {
        return repo.findById(id).map(existing -> {
            existing.setName(updatedAgency.getName());
            existing.setLocation(updatedAgency.getLocation());
            existing.setDomesticCylinders(updatedAgency.getDomesticCylinders());
            existing.setCommercialCylinders(updatedAgency.getCommercialCylinders());
            repo.save(existing);
            return true;
        }).orElse(false);
    }

    // ✅ Delete agency
    public boolean deleteGasAgency(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    // Helper: Entity → DTO
    private GasAgencyDTO mapToDTO(GasAgencyEntity e) {
        GasAgencyDTO dto = new GasAgencyDTO();
        dto.setGasid(e.getGasid());
        dto.setName(e.getName());
        dto.setLocation(e.getLocation());
        dto.setDomesticCylinders(e.getDomesticCylinders());
        dto.setCommercialCylinders(e.getCommercialCylinders());
        return dto;
    }

    // Helper: DTO → Entity
    private GasAgencyEntity mapToEntity(GasAgencyDTO dto) {
        GasAgencyEntity entity = new GasAgencyEntity();
        entity.setGasid(dto.getGasid());
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setDomesticCylinders(dto.getDomesticCylinders());
        entity.setCommercialCylinders(dto.getCommercialCylinders());
        return entity;
    }
}
