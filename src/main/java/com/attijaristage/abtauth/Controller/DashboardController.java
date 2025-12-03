package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.Entities.Effets;
import com.attijaristage.abtauth.Repository.ChequeRepository;
import com.attijaristage.abtauth.Repository.EffetsRepo;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import com.attijaristage.abtauth.Repository.VirementRepository;
import com.attijaristage.abtauth.Service.EffetService;
import com.attijaristage.abtauth.Service.KeycloakUserService;
import com.attijaristage.abtauth.Service.UserProfileService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private ChequeRepository chequeRepo ;
    @Autowired
    private UserProfileRepo userRepo;
    @Autowired
    private KeycloakUserService keycloakService;
    @Autowired
    private EffetsRepo effetsRepo;
    @Autowired
    private VirementRepository virementRepo ;
    Map<String,Double> percentage;
    @GetMapping("/effets-count")
    public Long getEffetsCount(){
        return effetsRepo.count();
    }
    @GetMapping("/user-count")
    public Long getUserCount(){
        return userRepo.count();
    }
    @GetMapping("/effets")
    public List<Effets> getEffets(){
        return effetsRepo.findAll();
    }
    @GetMapping("/effets/by-type")
    public List<Map<String, Object>> getEffetsGroupedByType() {
        List<Object[]> results = effetsRepo.countEffetsByType();
        List<Map<String,Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String,Object> map = new HashMap<>();
            map.put("typeEffet", row[0]);
            map.put("effectCount", row[1]);
            response.add(map);
        }
        return response;
    }
    @GetMapping("/cheques-count")
    public Long getChequesCount(){return chequeRepo.count();}
    @GetMapping("/cheques/by-status")
    public List<Map<String,Object>> getChequesGroupedByStatus(){
        List<Object[]> results=chequeRepo.countChequesBystatus();
        List<Map<String,Object>> response =new ArrayList<>();
        for (Object[] row : results){
            Map<String,Object> map = new HashMap<>();
            map.put("statusCheque",row[0]);
            map.put("chequeCount",row[1]);
            response.add(map);

        }
        return response;
    }

    @GetMapping("/effets-recent")
    public List<Object[]> getEffetsGroupedByDate(){
        return effetsRepo.countEffetsByDate();
    }
    @GetMapping("/effets/by-user")
    public List<Map<String, Object>> getEffetsByUser() {
        List<Object[]> results = effetsRepo.countEffetsByUser();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            String keycloakId = (String) row[0];
            map.put("effectCount", row[1]);

            UserRepresentation user = keycloakService.getUserById(keycloakId);
            if (user != null) {
                map.put("username", user.getUsername());
            }
            response.add(map);
        }
        return response;
    }
    @GetMapping("/cheques/by-user")
    public List<Map<String, Object>> getChequesByUser() {
        List<Object[]> results = chequeRepo.countChequesByUser();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            String keycloakId = (String) row[0];
            map.put("chequeCount", row[1]);

            UserRepresentation user = keycloakService.getUserById(keycloakId);
            if (user != null) {
                map.put("username", user.getUsername());
            }
            response.add(map);
        }
        return response;
    }
    @GetMapping("/virements/by-user")
    public List<Map<String, Object>> getVirementsByUser() {
        List<Object[]> results = virementRepo.countVirementsByUser();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            String keycloakId = (String) row[0];
            map.put("virementCount", row[1]);

            UserRepresentation user = keycloakService.getUserById(keycloakId);
            if (user != null) {
                map.put("username", user.getUsername());
            }
            response.add(map);
        }
        return response;
    }
    @GetMapping("effets/by-etat")
    public List<Object[]> getEffetByEtat(){
        return effetsRepo.countEffetsByEtat();
    }




}
