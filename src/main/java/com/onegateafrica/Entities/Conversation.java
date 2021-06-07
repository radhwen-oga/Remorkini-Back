package com.onegateafrica.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conversation {

    @Id
    private Long id;

    @OneToOne
    private Consommateur consommateur;

    @OneToOne
    private Remorqueur remorqueur;
    @OneToMany(fetch= FetchType.EAGER)
    private List<Message> messages;
}
