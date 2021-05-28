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
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="idRemorquer" )
  private Remorqueur remorqueur ;

  @Column(name = "description")
  private String description ;

  @Column(name = "finished")
  private boolean isFinished ;

  @Column(name = "declined")
  private boolean isDeclined =false ;

  @Column(name = "Clientpickedup")
  private boolean isClientPickedUp =false ;




}
