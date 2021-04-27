package com.onegateafrica.Entities;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Fonctionnalite")
public class Remorqueur extends Utilisateur {


  private String cinNumber;
  private String dateDebut;
  private String cinPhoto;

  public Remorqueur() {
  }

   /*@OneToMany(mappedBy = "remorqueur")
  Set<Course> listeDesCourses;*/
}
