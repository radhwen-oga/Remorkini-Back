package com.onegateafrica.Repositories;

import java.util.Optional;

import com.onegateafrica.Entities.Remorqueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RemorqueurRepository extends JpaRepository<Remorqueur,Long> {
  Optional<Remorqueur> findByCinNumber(String cinNumber);
  @Query(value = "SELECT * FROM RemorqueurLibre as r , Consommateur as c WHERE r.consommateur = c.id", nativeQuery = true)
  Optional<Remorqueur> findByPhoneNumber(String phoneNumber);

  @Modifying
  @Query("update Remorqueur u set u.isDisponible = :disponibility where u.id = :id")
  void updateDisponibility(@Param(value = "id") long id, @Param(value = "disponibility") boolean disponibility);
}
