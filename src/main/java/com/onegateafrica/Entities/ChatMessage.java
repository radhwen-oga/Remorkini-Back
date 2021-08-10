package com.onegateafrica.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Timestamp dateCreation ;

    private String contenu ;









    @OneToOne(cascade = CascadeType.ALL)
    private Consommateur user ;

    @JsonIgnore
    @ManyToOne
    private ChatConversation conversation ;

}
