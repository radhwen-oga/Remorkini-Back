package com.onegateafrica.Service;

import com.onegateafrica.Entities.Conversation;

import java.util.List;
import java.util.Optional;

public interface ConversationService {

    Optional<Conversation> findConversationByConIdAndRemId(Long consommateurId, Long remorqueurId);

    Conversation save(Conversation conversation);

    Optional<List<Conversation>> getAllConversation(Long id);

    Optional<Conversation> findById(Long id);
}
