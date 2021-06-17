package com.onegateafrica.Service;

import com.onegateafrica.Entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    Message save(Message message);
    Optional<List<Message>> filterMessage(Long id);
    int setSeen(Long conversationId, Long senderId);
    int setRecieved(Long recieverId);
}
