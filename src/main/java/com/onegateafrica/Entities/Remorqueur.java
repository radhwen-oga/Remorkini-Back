package com.onegateafrica.Entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "remorqeur")
public  class Remorqueur implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name ="cinnumber")
  private String cinNumber;

  @Column(name ="datedebut")
  private String dateDebut;

  @Column(name ="cinphoto")
  private String cinPhoto;

  @Column(name ="raisonsociale")
  private String raisonSociale;

  @Column(name ="activite")
  private String activite;

  @Column(name ="matriculeremorquage")
  private String matriculeRemorquage;

  @Column(name ="patentephoto")
  private String patentePhoto;

  @Column(name ="assurance")
  private String assurance;

  @Column(name="nombreDeVote")
  private Integer nombreDeVote=0;

  @Column(name="noteRemorqueurMoyenne",precision=8, scale=2)
  private double noteRemorqueurMoyenne=0;


  @OneToOne
  private Consommateur consommateur;

  @Column(name = "remorqeurType")
  @Enumerated(EnumType.STRING)
  private RemorqeurType remorqeurType;


  //ajout Radhwen ticket 1612
  @Column(name="isdisponible")
  private boolean isDisponible ;
  //////////////////////////
  @JsonBackReference
 @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "remorqueur",cascade = CascadeType.ALL)
  private List<DemandeRemorquage> listeDemandesRemorquage ;
}
