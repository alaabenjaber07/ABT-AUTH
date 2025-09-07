package com.attijaristage.abtauth.Service.Validation;

import com.attijaristage.abtauth.DTO.VirementDTO;
import com.attijaristage.abtauth.DTO.XmlVirementMapper;
import com.attijaristage.abtauth.Entities.XmlVirement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringWriter;

@Service
public class XmlValidationService {
    private final Schema virementSchema;
    private final JAXBContext jaxbContext;

    public XmlValidationService() throws Exception {
        var xsd = new ClassPathResource("xsd/virement.xsd");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.virementSchema = sf.newSchema(new StreamSource(xsd.getInputStream()));

        this.jaxbContext = JAXBContext.newInstance(XmlVirement.class);
    }

    public String marshalAndValidate(VirementDTO dto) throws Exception {
        XmlVirement xmlObj = XmlVirementMapper.fromDto(dto);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setSchema(virementSchema);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(xmlObj, sw); // إذا ما يطابقش XSD يرمي SAXException
        return sw.toString();
    }

}
