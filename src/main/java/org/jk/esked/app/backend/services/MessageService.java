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

    public void deleteMessageById(UUID id) {
        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesForUser(UUID userId) {
        return messageRepository.getMessagesForUser(userId);
    }

//    public void setCheckedFlag(UUID messageId) {
//        messageRepository.setCheckedFlag(messageId, true);
//    }

//    public boolean doesMessageIdExists(UUID newMessageId) {
//        return !messageRepository.doesMessageIdExists(newMessageId).isEmpty();
//    }
}
