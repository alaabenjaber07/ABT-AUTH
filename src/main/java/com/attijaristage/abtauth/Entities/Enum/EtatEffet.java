package com.attijaristage.abtauth.Entities.Enum;

public enum EtatEffet {
    EN_ATTENTE("en_attente"),
    PAYE("paye"),
    REJETE("rejete"),
    IMPAYE("impaye"),
    ANNULE("annule"),
    ECHU("echu"); //entre en attente et impaye
    private String value;
    EtatEffet(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
