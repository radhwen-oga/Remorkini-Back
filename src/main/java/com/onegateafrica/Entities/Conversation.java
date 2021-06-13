package com.onegateafrica.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conversation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToOne
    private Consommateur consommateur;
    @NotNull
    @OneToOne
    private Consommateur remorqueur;

    private Date lastActivity;

    @JsonIgnore
    @OneToMany(fetch= FetchType.LAZY, mappedBy = "conversation")
    private List<Message> messages;
}
