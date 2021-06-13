package com.onegateafrica.Repositories;


import com.onegateafrica.Entities.Conversation;
import com.onegateafrica.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM message WHERE id_conversation = :conversationId  ORDER BY timestamp ASC", nativeQuery = true)
    Optional<List<Message>> filterMessage(@Param(value="conversationId") Long conversationId);

}

