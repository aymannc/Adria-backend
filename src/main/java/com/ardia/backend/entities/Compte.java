package com.ardia.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroCompte;
    private String intitule;
    private BigDecimal soldeComptable;
    @ManyToOne
    private Abonne abonne;

    @OneToMany(mappedBy = "compte")
    private Collection<Virment> virments;
}
