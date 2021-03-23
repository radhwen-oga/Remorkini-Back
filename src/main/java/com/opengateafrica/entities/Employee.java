package com.opengateafrica.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Employee")
public class Employee {
	@Id
	public long id;
	
	private String num_cin;
	
	private String raison_sociale;
	
	private String activite;
	
	private String a_compter_du;
	
	private String matricule_remorquage;
	
	private String imagePatente;
}