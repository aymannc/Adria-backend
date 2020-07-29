package com.ardia.backend;

import com.ardia.backend.entities.Abonne;
import com.ardia.backend.entities.Compte;
import com.ardia.backend.repositories.AbonneRepository;
import com.ardia.backend.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class VirementMultipleAppApplication implements CommandLineRunner {
    @Autowired
    AbonneRepository abonneRepository;
    @Autowired
    CompteRepository compteRepository;


    public static void main(String[] args) {
        SpringApplication.run(VirementMultipleAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
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

        
    }
}
