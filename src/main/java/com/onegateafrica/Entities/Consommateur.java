package com.onegateafrica.Entities;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Consommateur extends User {

    @Column(name = "numeroinscription")
    private String numeroInscription;


    @JsonBackReference
    @OneToOne(mappedBy = "consommateur")
    private Remorqueur remorqueur;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    /*@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consommateur",cascade = CascadeType.ALL  )
    private List<DemandeRemorquage> listeDemandesRemorquage ;*/
}
