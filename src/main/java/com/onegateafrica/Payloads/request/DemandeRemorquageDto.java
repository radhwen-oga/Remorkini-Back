package com.onegateafrica.Payloads.request;

import com.onegateafrica.Entities.Consommateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeRemorquageDto {

  private String description ;
  private Long idConsommateur ;
  private String marqueVoiture;
  private String nbrePersonnes ;
  private String typePanne ;

}