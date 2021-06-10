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
@Table(name = "bannissement")
public class Bannissement {

    @Id
    @GeneratedValue
    private Long id ;

    @Column(name = "nbrjoursbann")
    private long nbrJoursBann ;

    @Column(name = "datedebutbann")
    private Date dateDebutBann ;

    @Column(name = "datefinbann")
    private Date dateFinBann ;

    @JsonIgnore
    @ManyToOne
    private Remorqueur remorqueur ;


}
