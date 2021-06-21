package com.onegateafrica.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Location {
    @Id
    @GeneratedValue
    private Long id ;


    private String nom ;
    private Double longitude ;
    private Double lattitude ;



    public Location(Double lattitude, Double longitude) {
        this.longitude = longitude;
        this.lattitude = lattitude;
    }
}
