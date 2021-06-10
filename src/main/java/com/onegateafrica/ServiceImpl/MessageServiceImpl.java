package com.onegateafrica.ServiceImpl;

import com.onegateafrica.Entities.Conversation;
import com.onegateafrica.Entities.Message;
import com.onegateafrica.Repositories.MessageRepository;
import com.onegateafrica.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {


	private final MessageRepository messageRepository;
	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository){
		this.messageRepository=messageRepository;

	}
	@Override
	public Message save(Message message) {
		return this.messageRepository.save(message);
	}

	@Override
	public Optional<List<Message>> filterMessage(Long id) {
		return messageRepository.filterMessage(id);
	}
}
