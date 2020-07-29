package com.ardia.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirmentMultiple extends Virment {
    private Integer nombreBeneficiaires;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "virmentMultiple")
    private Collection<VirmentMultipleBeneficiaire> virmentMultipleBeneficiaires;
}
