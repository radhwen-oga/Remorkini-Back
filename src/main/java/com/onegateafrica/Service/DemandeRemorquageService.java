package com.onegateafrica.Service;

import com.onegateafrica.Payloads.response.VerificationChangementRemorqeurResponse;

public interface DemandeRemorquageService {
    VerificationChangementRemorqeurResponse permettreChangementRemorqueur(Long idDemande);
}
