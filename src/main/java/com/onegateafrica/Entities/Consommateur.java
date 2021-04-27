package com.onegateafrica.Entities;





import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Consommateur")
public class Consommateur extends Utilisateur {
    private String  numeroInscription;



    /*@OneToMany(mappedBy = "client")
    Set<Course> listeDesCourses;*/
	

	
    
}
