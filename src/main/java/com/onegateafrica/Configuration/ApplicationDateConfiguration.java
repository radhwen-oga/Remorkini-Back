package com.onegateafrica.Configuration;

import com.onegateafrica.Entities.ApplicationConfiguration;
import com.onegateafrica.Repositories.ApplicationConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Configuration
public class ApplicationDateConfiguration {
    private final ApplicationConfigurationRepository applicationConfigurationRepository ;



    @Autowired
    public ApplicationDateConfiguration(ApplicationConfigurationRepository applicationConfigurationRepository) {
        this.applicationConfigurationRepository = applicationConfigurationRepository;
    }

    @Bean
    public void configureApplicationDate () throws ParseException {
        List<ApplicationConfiguration> configurationList = applicationConfigurationRepository.findAll();

        //a la premiére fois du lancement de l'application on met la date de debut et fin de semaine
        if(configurationList.isEmpty()){
            ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
            Date today = new Date();
            Instant now =Instant.now();
            Instant weekAfter = now.plus(Duration.ofDays(7)) ;
            Date dateWeekAfter = Date.from(weekAfter);

            applicationConfiguration.setDateLancementApplication(today);
            applicationConfiguration.setDateDebutDeSemaine(today);
            applicationConfiguration.setDateFinDeSemaine(dateWeekAfter);

            System.out.println(applicationConfiguration.getDateDebutDeSemaine()+" "+applicationConfiguration.getDateFinDeSemaine());
            applicationConfigurationRepository.save(applicationConfiguration);

        }

        //on vérifie si on a atteint la fin de semaine
        else {

            Date today  =new Date();
            Instant now =Instant.now();


            ApplicationConfiguration applicationConfiguration = configurationList.get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date todayWithoutTime = sdf.parse(sdf.format(today));

            Date dateFinDeSemaineWithoutTime = sdf.parse(sdf.format(applicationConfiguration.getDateFinDeSemaine()));

            //System.out.println("resultat de comparaison "+todayWithoutTime.compareTo(dateFinDeSemaineWithoutTime));

            //si on a atteint la fin de semaine => reinitialiser la semaine
            
            if(todayWithoutTime.compareTo(dateFinDeSemaineWithoutTime)==0){
                Instant newDateFinDeSemaine = now.plus(Duration.ofDays(7)) ;

                applicationConfiguration.setDateDebutDeSemaine(Date.from(now));
                applicationConfiguration.setDateFinDeSemaine(Date.from(newDateFinDeSemaine));
                applicationConfigurationRepository.save(applicationConfiguration);
            }

        }

    }
}
