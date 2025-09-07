package com.attijaristage.abtauth.Service;
import com.attijaristage.abtauth.DTO.EffetsDTO;
import com.attijaristage.abtauth.DTO.EffetsMapper;
import com.attijaristage.abtauth.DTO.EffetsWrapperDTO;
import com.attijaristage.abtauth.Entities.Effets;
import com.attijaristage.abtauth.Entities.Enum.EtatEffet;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.EffetsRepo;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EffetService {
    @Autowired
    private EffetsRepo repo;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileRepo userProfileRepo;
    private static final String XSD_PATH = "/xsd/effets.xsd";

    public Effets create(EffetsDTO dto, String keycloakId) throws Exception {
        Effets effet = EffetsMapper.toEntity(dto);


        Long userId = userProfileService.getIdByKeycloakId(keycloakId);
        UserProfile user = userProfileRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        effet.setDateAjout(LocalDate.now()); // date actuelle
        effet.setUser(user); // many-to-one vers l'utilisateur

        return repo.save(effet);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
    public List<EffetsDTO> getAll() {
        List<Effets> effets = repo.findAll();
        return effets.stream()
                .map(EffetsMapper::toDTO)
                .toList();
    }

    public Optional<Effets> getById(Long id){
        return repo.findById(id);
    }
    public void deleteMultiple(List<Long>ids){
        for(Long id : ids){
            repo.deleteById(id);
        }
    }
    public void updateEtat(Long id, String nouvelEtat){
        Optional<Effets> effetOpt=repo.findById(id);
        if (effetOpt.isPresent()) {
            Effets effet = effetOpt.get();
            effet.setEtat(EtatEffet.valueOf(nouvelEtat.toUpperCase()));
            repo.save(effet);
        } else {
            throw new EntityNotFoundException("Effet avec ID " + id + " introuvable.");
        }
    }
    public String convertToXml(List<EffetsDTO> dtos) throws Exception {
        EffetsWrapperDTO wrapper = new EffetsWrapperDTO();
        wrapper.setEffets(dtos);

        JAXBContext context = JAXBContext.newInstance(EffetsWrapperDTO.class, EffetsDTO.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(wrapper, sw);
        return sw.toString();
    }

    public void validateXml(String xml) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(getClass().getResource(XSD_PATH));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }
    public List<Effets> convertToObjectsAndSave(String xml) throws Exception {
        validateXml(xml);

        JAXBContext context = JAXBContext.newInstance(EffetsWrapperDTO.class, EffetsDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        EffetsWrapperDTO wrapper = (EffetsWrapperDTO) unmarshaller.unmarshal(new StringReader(xml));

        List<Effets> effets = wrapper.getEffets().stream()
                .map(EffetsMapper::toEntity)
                .toList();

        return repo.saveAll(effets);
    }





}
