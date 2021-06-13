package com.onegateafrica.Repositories;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.Conversation;
import com.onegateafrica.Entities.Remorqueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {



	@Query(value = "SELECT * FROM Conversation WHERE consommateur_id = :consommateurId AND remorqueur_id = :remorqueurId", nativeQuery = true)
	Optional<Conversation> findConversation(@Param(value="consommateurId") Long consommateurId, @Param(value="remorqueurId") Long remorqueurId);
	@Query(value = "SELECT * FROM Conversation WHERE consommateur_id = :consommateurId OR remorqueur_id = :consommateurId", nativeQuery = true)
	Optional<List<Conversation>> findAllConversations(@Param(value="consommateurId") Long consommateurId);
}

