package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.VirementDTO;
import com.attijaristage.abtauth.DTO.VirementMapper;
import com.attijaristage.abtauth.Entities.Enum.StatusVirement;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Entities.Virement;
import com.attijaristage.abtauth.Repository.VirementRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VirementService {
    @Autowired
    private VirementRepository virementRepository;

    public VirementDTO ajouterVirement(VirementDTO dto, UserProfile user) {
        Virement virement = new Virement();
        BeanUtils.copyProperties(dto, virement);
        virement.setUser(user);
        Virement saved = virementRepository.save(virement);
        VirementDTO responseDTO = new VirementDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        responseDTO.setUser(user);
        return responseDTO;

    }

    public List<VirementDTO> getAllVirementsDTO() {
        List<Virement> virements = virementRepository.findAll();
        return virements.stream()
                .map(VirementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteVirement(Long id) {
        if (!virementRepository.existsById(id)) {
            throw new RuntimeException("Le virement avec l'ID " + id + " n'existe pas.");
        }
        virementRepository.deleteById(id);
    }

    public VirementDTO updateVirement(Long id, String status) {
        Optional<Virement> existingOpt = virementRepository.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Virement introuvable avec l'ID : " + id);
        }

        Virement existing = existingOpt.get();
        existing.setStatus(StatusVirement.valueOf(status));

        // Sauvegarde
        Virement saved = virementRepository.save(existing);

        return VirementMapper.toDTO(saved);
    }



    // 1) Nombre total de virements
    public long getNombreVirementTotale() {
        return virementRepository.getNombreVirementTotale();
    }

    public long getNombreToday() {
        LocalDate today = LocalDate.now();
        return virementRepository.countByDate(today);
    }

    public long getNombreThisMonth() {
        LocalDate today = LocalDate.now();
        return virementRepository.countByMonth(today.getMonthValue(), today.getYear());
    }

    public long getNombreThisYear() {
        LocalDate today = LocalDate.now();
        return virementRepository.countByYear(today.getYear());
    }

    // 3) Historique des virements
    public List<VirementDTO> getHistoriqueVirement() {
        List<Virement> virements = virementRepository.findAllByOrderByDateVirementDesc();
        return virements.stream()
                .map(v -> {
                    VirementDTO dto = new VirementDTO();
                    BeanUtils.copyProperties(v, dto);
                    return dto;
                }).collect(Collectors.toList());
    }

    // 4) Nombre de virements par instruction
    public Map<String, Long> getNombreVirementsParInstruction() {
        List<Object[]> results = virementRepository.countVirementsByInstruction();
        Map<String, Long> stats = new HashMap<>();
        for (Object[] row : results) {
            stats.put(row[0].toString(), (Long) row[1]);
        }
        return stats;
    }




}
