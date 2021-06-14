package com.onegateafrica.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class DemandeRemorquage implements Serializable {


  @Id
  @GeneratedValue
  private Long id ;


  @ManyToOne()
  @JoinColumn(name="idConsommateur", nullable=false )
  private Consommateur consommateur ;

  @JsonIgnore
  @ManyToOne()
  @JoinColumn(name="idRemorquer" )
  private Remorqueur remorqueur ;

  @Column(name = "description")
  private String description ;

  @Column(name = "finished")
  private Boolean isFinished ;

  @Column(name = "declined")
  private Boolean isDeclined  ;

  @Column(name = "Clientpickedup")
  private Boolean isClientPickedUp  ;

  private String marqueVoiture;
  private String nbrePersonnes ;
  private String typePanne ;

  @Column(name = "canceledbyclient")
  private Boolean isCanceledByClient ;




}
