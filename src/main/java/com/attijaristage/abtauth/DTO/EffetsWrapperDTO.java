package com.attijaristage.abtauth.DTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "effets")
@XmlAccessorType(XmlAccessType.FIELD)
public class EffetsWrapperDTO {
    @XmlElement(name = "effet")
    private List<EffetsDTO> effets;


    public List<EffetsDTO> getEffets() {
        return effets;
    }

    public void setEffets(List<EffetsDTO> effets) {
        this.effets = effets;
    }
}
