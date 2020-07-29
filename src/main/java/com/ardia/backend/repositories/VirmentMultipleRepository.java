package com.ardia.backend.repositories;

import com.ardia.backend.entities.VirmentMultiple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VirmentMultipleRepository extends JpaRepository<VirmentMultiple, Long> {
}