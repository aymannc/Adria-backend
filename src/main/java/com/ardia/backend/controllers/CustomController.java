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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    @PostMapping("/filter-virement")
    public ResponseEntity<List<VirmentMultiple>> filterVirement(@RequestBody FiltringForm filtringForm){
        Compte compte = compteRepository.findById(filtringForm.getCompteNumero()).get();
        if(compte == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this compte"
            );
        }

        List<VirmentMultiple> virmentMultiples = new ArrayList<>();
        compte.getVirments().forEach(virment -> {

            boolean dateFilter = false || filtringForm.getDateExecution().equals("");
            boolean montantMaxFilter =false;
            boolean montantMinFilter = false;

            if(virment.getDateExcecution() != null && !filtringForm.getDateExecution().equals("")){
                dateFilter = filtringForm.getDateExecution().equals(virment.getDateExcecution().toString().split(" ")[0]);
            }
            montantMaxFilter = virment.getMontant().compareTo(filtringForm.getMontantMax())==-1 || virment.getMontant().compareTo(filtringForm.getMontantMax())==-0;
            montantMinFilter = virment.getMontant().compareTo(filtringForm.getMontantMin())==1 || virment.getMontant().compareTo(filtringForm.getMontantMax())==-0;

            System.out.println("Date Filter :"+dateFilter);
            System.out.println("Max :"+montantMaxFilter);
            System.out.println("Min :"+montantMinFilter);
            if(montantMaxFilter  && montantMinFilter){
                virmentMultiples.add((VirmentMultiple) virment);
            }
        });

        return new ResponseEntity<>(virmentMultiples,HttpStatus.OK);
    }
    @PostMapping("/delete-virement/{id}")
    public ResponseEntity<Long> deleteVirment(@PathVariable(value = "id")Long id){
        VirmentMultiple virmentMultiple = virmentMultipleRepository.findById(id).get();
        if(virmentMultiple == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this virement"
            );
        }
        virmentMultiple.getVirmentMultipleBeneficiaires().forEach(b->{
            virmentMultipleBeneficiaireRepository.delete(b);
        });
        virmentMultipleRepository.delete(virmentMultiple);
        return  new ResponseEntity<>(virmentMultiple.getId(),HttpStatus.OK);
    }
    @GetMapping("/get-virement/{id}")
    public ResponseEntity<VirementForm> getVirement(@PathVariable(value = "id")Long id){
        VirmentMultiple virmentMultiple = virmentMultipleRepository.findById(id).get();
        if(virmentMultiple == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this virement"
            );
        }
        VirementForm virementForm = new VirementForm();
        virementForm.setId(virmentMultiple.getId());
        virementForm.setAccountNumber(virmentMultiple.getCompte().getNumeroCompte());
        virementForm.setMotif(virmentMultiple.getMotif());
        virementForm.setMontant(virmentMultiple.getMontant());
        virementForm.setNbrOfBenf(virmentMultiple.getNombreBeneficiaires());
        List<SelectedBeneficiaire> beneficiaires = new ArrayList<>();
        virmentMultiple.getVirmentMultipleBeneficiaires().forEach(b ->{
            SelectedBeneficiaire selectedBeneficiaire = new SelectedBeneficiaire();
            selectedBeneficiaire.setId(b.getBeneficiaire().getId());
            selectedBeneficiaire.setMontant(b.getMontant().toString());
            beneficiaires.add(selectedBeneficiaire);
        });
        virementForm.setSelectedBeneficiaire(beneficiaires);
        virementForm.setCreatedDate(virmentMultiple.getDateCreation());
        return new ResponseEntity<>(virementForm,HttpStatus.OK);
    }

    @PostMapping("/modifier-virement")
    public ResponseEntity<Long> modifierVirement(@RequestBody VirementForm requestBody){
        VirmentMultiple virmentMultiple = virmentMultipleRepository.findById(requestBody.getId()).get();
        if(virmentMultiple == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Didn't found this virement"
            );
        }
        virmentMultiple.setCompte(compteRepository.findByNumeroCompte(requestBody.getAccountNumber()));
        virmentMultiple.setNombreBeneficiaires(requestBody.getNbrOfBenf());
        virmentMultiple.setMotif(requestBody.getMotif());
        virmentMultiple.getVirmentMultipleBeneficiaires().forEach(b ->{
            virmentMultipleBeneficiaireRepository.delete(b);
        });
        List<VirmentMultipleBeneficiaire> beneficiaires = new ArrayList<>();
        requestBody.getSelectedBeneficiaire().forEach(selectedBeneficiaire -> {
            VirmentMultipleBeneficiaire virmentMultipleBeneficiaire = new VirmentMultipleBeneficiaire();
            virmentMultipleBeneficiaire.setBeneficiaire(beneficiaireRepository.getOne(selectedBeneficiaire.getId()));
            virmentMultipleBeneficiaire.setVirmentMultiple(virmentMultiple);
            virmentMultipleBeneficiaire.setMontant(new BigDecimal(selectedBeneficiaire.getMontant()));
            virmentMultipleBeneficiaireRepository.save(virmentMultipleBeneficiaire);
            beneficiaires.add(virmentMultipleBeneficiaire);
        });

        virmentMultiple.setVirmentMultipleBeneficiaires(beneficiaires);
        return new ResponseEntity<>(virmentMultiple.getId(),HttpStatus.OK);
    }
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
class FiltringForm{
    private Long compteNumero;
    private String dateExecution;
    private BigDecimal montantMax;
    private BigDecimal montantMin;
    private String statut;
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
    private Long id;
    private String accountNumber;
    private Date createdDate;
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
