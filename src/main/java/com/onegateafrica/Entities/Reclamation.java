package com.onegateafrica.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "reclamation")
public class Reclamation {

    @Id
    @GeneratedValue
    private Long id ;

    @Column(name = "description")
    private String description ;

    @Column(name = "typereclamation")
    @Enumerated(EnumType.STRING)
    private ETypeReclamation typeReclamation ;

    @Column(name="dateajout")
    private Date dateAjout ;

    @JsonIgnore
    @ManyToOne
    private Remorqueur remorqueur;

}
