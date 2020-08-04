package com.ardia.backend.repositories;

import com.ardia.backend.entities.Abonne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface AbonneRepository extends JpaRepository<Abonne, Long> {
    public Abonne findByUsername(String username);
}
