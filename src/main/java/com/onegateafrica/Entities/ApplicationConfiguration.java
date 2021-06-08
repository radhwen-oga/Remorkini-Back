package com.onegateafrica.Entities;

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
@Table(name = "applicationconfiguration")
public class ApplicationConfiguration {
    @Id
    @GeneratedValue
    private Long id ;

    @Column(name = "dateLancementApplication")
    private Date dateLancementApplication ;

    @Column(name = "datedebutdesemaine")
    private Date dateDebutDeSemaine ;

    @Column(name = "datefindesemaine")
    private Date dateFinDeSemaine ;

    @Column(name = "isapplicationlaunched")
    private Boolean isApplicationLaunched ;
}
