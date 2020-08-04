package com.ardia.backend.repositories;

import com.ardia.backend.entities.VirmentMultipleBeneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface VirmentMultipleBeneficiaireRepository extends JpaRepository<VirmentMultipleBeneficiaire, Long> {
}
