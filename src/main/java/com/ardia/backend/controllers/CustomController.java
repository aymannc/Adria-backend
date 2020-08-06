package com.ardia.backend.controllers;

import com.ardia.backend.entities.*;
import com.ardia.backend.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Controller
public class CustomController {

    @Autowired
    CompteRepository compteRepository;
    @Autowired
    VirmentMultipleRepository virmentMultipleRepository;
    @Autowired
    BeneficiaireRepository beneficiaireRepository;
    @Autowired
    AbonneRepository abonneRepository;
    @Autowired
    VirmentMultipleBeneficiaireRepository virmentMultipleBeneficiaireRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/ajouter-virement")
    public ResponseEntity<Long> createUser(@RequestBody VirementForm requestBody) {

        Compte compte = compteRepository.findByNumeroCompte(requestBody.getAccountNumber());
        if (compte == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this account"
            );
        }

        List<VirmentMultipleBeneficiaire> virmentMultipleBeneficiaires = new ArrayList<>();
        Abonne abonne = compte.getAbonne();
        VirmentMultiple virmentMultiple = new VirmentMultiple();

        virmentMultiple.setMotif(requestBody.getMotif());
        virmentMultiple.setMontant(requestBody.getMontant());
        virmentMultiple.setDateExcecution(null);
        virmentMultiple.setAbonne(abonne);
        virmentMultiple.setCompte(compte);
        virmentMultiple.setNombreBeneficiaires(requestBody.getNbrOfBenf());

        final VirmentMultiple virmentMultipleCopy = virmentMultipleRepository.save(virmentMultiple);

        requestBody.getSelectedBeneficiaire().forEach(selectedBeneficiaire -> {
            Beneficiaire beneficiaire = beneficiaireRepository.findById(selectedBeneficiaire.getId()).orElse(
                    null
            );
            if (beneficiaire == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Didn't found this beneficiary"
                );
            }
            virmentMultipleBeneficiaires.add(virmentMultipleBeneficiaireRepository.save(new VirmentMultipleBeneficiaire(null,
                    new BigDecimal(selectedBeneficiaire.getMontant()), beneficiaire, virmentMultipleCopy)));
        });
        virmentMultiple.setVirmentMultipleBeneficiaires(virmentMultipleBeneficiaires);
        virmentMultipleRepository.save(virmentMultiple);
        return new ResponseEntity<>(virmentMultipleRepository.save(virmentMultiple).getId(), HttpStatus.CREATED);
    }

    @PostMapping("/sign")
    public ResponseEntity<Boolean> signVirement(@RequestBody SignForm form) throws ResponseStatusException {
        System.out.println(form);
        VirmentMultiple virment = virmentMultipleRepository.findById(form.getVirmentID())
                .orElse(null);
        if (virment == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this virment"
            );
        }
        if (virment.getDateExcecution() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This virement already singed "
            );
        }
        Abonne abonne = abonneRepository.findByUsername(form.getUsername());
        if (abonne == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this user"
            );
        }
        boolean didMatch = passwordEncoder.matches(form.getPassword(), abonne.getPassword());
        if (!didMatch) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Password didn't match"
            );
        }
        virment.setDateExcecution(new Date());
        virmentMultipleRepository.save(virment).getId();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class SignForm {
    private String username;
    private String password;
    private Long virmentID;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class VirementForm {
    private String accountNumber;
    private LocalDate createdDate;
    private BigDecimal montant;
    private String motif;
    private Integer nbrOfBenf;
    private List<SelectedBeneficiaire> selectedBeneficiaire;
}

@Data
class SelectedBeneficiaire {
    private String montant;
    private long id;
}
