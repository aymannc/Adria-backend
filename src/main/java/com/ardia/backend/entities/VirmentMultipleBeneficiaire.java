package com.ardia.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirmentMultipleBeneficiaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal montant;

    @ManyToOne
    private Beneficiaire beneficiaire;
    @ManyToOne
    private VirmentMultiple virmentMultiple;
}
