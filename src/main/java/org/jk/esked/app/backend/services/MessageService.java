package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.model.entities.User;
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

    public void saveMessage(User user, String text) {
        messageRepository.save(new Message(user, text));
    }

    public List<Message> getAllMessagesForUserSortedByDate(UUID userId) {
        return messageRepository.getAllMessagesForUserSortedByDate(userId);
    }

    public void deleteMessageById(UUID id) {
        messageRepository.deleteById(id);
    }

    public void changeCheckedFlagByMessageId(UUID id, boolean state) {
        messageRepository.ChangeCheckedFlagByMessageId(id, state);
    }
}
