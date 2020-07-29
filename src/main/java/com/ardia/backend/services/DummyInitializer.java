package com.ardia.backend.services;

import com.ardia.backend.entities.*;
import com.ardia.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DummyInitializer implements Initializer {
    @Autowired
    AbonneRepository abonneRepository;
    @Autowired
    CompteRepository compteRepository;
    @Autowired
    VirmentMultipleRepository virmentMultipleRepository;
    @Autowired
    BeneficiaireRepository beneficiaireRepository;
    @Autowired
    VirmentMultipleBeneficiaireRepository virmentMultipleBeneficiaireRepository;

    @Override
    public void initAllData() {
        Abonne abonne1 = abonneRepository.save(
                new Abonne(null, "anc", "1234", "Nait", "Ayman", null,
                        null));
        Abonne abonne2 = abonneRepository.save(
                new Abonne(null, "med", "1234", "ouftou", "mohmad", null,
                        null));
        Abonne abonne3 = abonneRepository.save(
                new Abonne(null, "user", "1234", "Chtaiba", "kk", null,
                        null));

        compteRepository.save(
                new Compte(null, "1", "Mr", new BigDecimal("1000"), abonne1));
        compteRepository.save(
                new Compte(null, "2", "Mr", new BigDecimal("10000"), abonne1));
        compteRepository.save(new Compte(null, "3", "Mr", new BigDecimal("-10"), abonne1));

        List<Beneficiaire> beneficiairesList = new ArrayList<>();

        beneficiairesList.add(
                new Beneficiaire(null, "Nait Cherif", "Ayman", 1, null)
        );
        beneficiairesList = beneficiaireRepository.saveAll(beneficiairesList);
        abonne2.setBeneficiaires(beneficiairesList);
        abonneRepository.save(abonne2);

        VirmentMultiple virmentMultiple = new VirmentMultiple(1, null);
        virmentMultiple.setAbonne(abonne2);
        virmentMultiple.setDateExcecution(new Date());
        virmentMultiple.setMontant(new BigDecimal(1000));
        virmentMultiple.setMotif("Motif");
        virmentMultiple = virmentMultipleRepository.save(virmentMultiple);

        VirmentMultipleBeneficiaire vmb = new VirmentMultipleBeneficiaire(null, new BigDecimal(1000),
                beneficiairesList.get(0), virmentMultiple);
        virmentMultipleBeneficiaireRepository.save(vmb);
    }
}
