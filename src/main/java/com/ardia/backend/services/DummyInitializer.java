package com.ardia.backend.services;

import com.ardia.backend.entities.*;
import com.ardia.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DummyInitializer implements Initializer {
    @Autowired
    AccountService accountService;
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
        Abonne abonne1 = accountService.saveUser(
                new Abonne(null, "nait_cherif", "1234", "Nait", "Ayman", null,
                        null));
        Abonne abonne2 = accountService.saveUser(
                new Abonne(null, "ouftou", "1234", "ouftou", "mohmad", null,
                        null));
        Abonne abonne3 = accountService.saveUser(
                new Abonne(null, "oulahyane", "1234", "Oulahyane", "Kaoutar", null,
                        null));
        Abonne abonne4 = accountService.saveUser(
                new Abonne(null, "sabar", "1234", "Sabar", "Laila", null,
                        null));

        compteRepository.save(
                new Compte(null, "123342", "Mr", new BigDecimal("1000"), abonne1, null));
        Compte compte = new Compte(null, "435632", "Mr", new BigDecimal("10000"), abonne1, null);
        compteRepository.save(
                compte);
        compteRepository.save(new Compte(null, "3980874", "Mr", new BigDecimal("-10"), abonne2, null));

        List<Beneficiaire> beneficiairesList = new ArrayList<>();

        beneficiairesList.add(
                new Beneficiaire(null, "Ouftou", "Mohamed", 3, null)
        );
        beneficiairesList = beneficiaireRepository.saveAll(beneficiairesList);
        abonne1.setBeneficiaires(beneficiairesList);
        accountService.saveUser(abonne1);

        VirmentMultiple virmentMultiple = new VirmentMultiple(1, null);
        virmentMultiple.setAbonne(abonne2);
        virmentMultiple.setCompte(compte);
        virmentMultiple.setDateExcecution(null);
        virmentMultiple.setStatut("Enregistré");
        virmentMultiple.setMontant(new BigDecimal(1000));
        virmentMultiple.setMotif("Motif");
        virmentMultiple = virmentMultipleRepository.save(virmentMultiple);

        VirmentMultipleBeneficiaire vmb = new VirmentMultipleBeneficiaire(null, new BigDecimal(1000),
                beneficiairesList.get(0), virmentMultiple);
        virmentMultipleBeneficiaireRepository.save(vmb);
        abonneRepository.findAll().forEach(user -> {
            System.out.println("username : " + user.getUsername());
            System.out.println("password : " + user.getPassword());
        });
    }
}
