package com.ardia.backend.entities;

import com.ardia.backend.entities.Compte;
import com.ardia.backend.entities.VirmentMultiple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Projection(name = "virProj",types = VirmentMultiple.class)
public interface VirementMultipleProjection {
    public Long getId();
    public Date getDateCreation();
    public Date getDateExcecution();
    public BigDecimal getMontant();
    public String getMotif();
    public Compte getCompte();
    public Abonne getAbonne();
    public Collection<VirmentMultipleBeneficiaire> getVirmentMultipleBeneficiaires();
}
