package com.ardia.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Virment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @CreationTimestamp
    private Date dateCreation;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateExcecution;
    private BigDecimal montant;
    private String motif;

    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Compte compte;
}
