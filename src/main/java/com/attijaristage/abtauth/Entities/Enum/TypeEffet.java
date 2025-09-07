package com.attijaristage.abtauth.Entities.Enum;

public enum TypeEffet {
    LETTRE_DE_CHANGE("lettre_de_change"),
    BILLET_A_ORDRE("billet_a_ordre");
    private String value;
    TypeEffet(String value){this.value=value;}

    @Override
    public String toString(){
        return value;
    }
}
