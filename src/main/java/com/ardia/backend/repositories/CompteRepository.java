package com.ardia.backend.repositories;


import com.ardia.backend.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Compte findByNumeroCompte(String numeroCompte);
}
