package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.Message;
import org.jk.esked.app.backend.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessagesForUser(UUID userId) {
        return messageRepository.getMessagesForUser(userId);
    }

    public void deleteMessageById(UUID id) {
        messageRepository.deleteById(id);
    }

    public void changeCheckedFlagByMessageId(UUID id, boolean state) {
        messageRepository.setCheckedFlagByMessageId(id, state);
    }
}
