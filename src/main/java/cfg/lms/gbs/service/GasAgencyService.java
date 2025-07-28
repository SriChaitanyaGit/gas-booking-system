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

    public GasAgencyDTO createAgency(GasAgencyDTO dto) {
        GasAgencyEntity entity = new GasAgencyEntity();
        entity.setGasid(dto.getGasid());
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());

        GasAgencyEntity saved = repo.save(entity);

        GasAgencyDTO result = new GasAgencyDTO();
        result.setGasid(saved.getGasid());
        result.setName(saved.getName());
        result.setLocation(saved.getLocation());
        return result;
    }

    public List<GasAgencyDTO> fetchAllAgencies() {
        return repo.findAll().stream().map(e -> {
            GasAgencyDTO dto = new GasAgencyDTO();
            dto.setGasid(e.getGasid());
            dto.setName(e.getName());
            dto.setLocation(e.getLocation());
            return dto;
        }).collect(Collectors.toList());
    }

    public GasAgencyDTO fetchAgencyById(int id) {
        Optional<GasAgencyEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            GasAgencyEntity e = opt.get();
            GasAgencyDTO dto = new GasAgencyDTO();
            dto.setGasid(e.getGasid());
            dto.setName(e.getName());
            dto.setLocation(e.getLocation());
            return dto;
        } else {
            return null;
        }
    }

    public GasAgencyDTO updateAgency(int id, GasAgencyDTO dto) {
        GasAgencyEntity existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Agency not found"));
        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());

        GasAgencyEntity updated = repo.save(existing);
        GasAgencyDTO result = new GasAgencyDTO();
        result.setGasid(updated.getGasid());
        result.setName(updated.getName());
        result.setLocation(updated.getLocation());
        return result;
    }

    public void deleteAgency(int id) {
        repo.deleteById(id);
    }
}
