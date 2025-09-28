package com.attijaristage.abtauth.Entities.Enum;

public enum ChequeStatus {
    EN_COURS,   // Chèque en cours, en attente d'action (encaissement ou rejet)
    ACCEPTE,    // Chèque accepté, encaissé avec succès
    REJETE      // Chèque rejeté, par exemple pour fonds insuffisants
}

