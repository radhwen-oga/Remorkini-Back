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
@DiscriminatorValue("Libre")
public class RemorqueurLibre extends Remorqueur  {


	private String raisonSociale;
	private String activite;
	private String matriculeRemorquage;
	private String patentePhoto;

}
