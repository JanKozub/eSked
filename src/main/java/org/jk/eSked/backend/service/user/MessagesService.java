package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.MessagesDao;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.repositories.MessagesDB;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class MessagesService implements MessagesDB {

    private final MessagesDao messagesDao;

    public MessagesService(MessagesDao messagesDao) {
        this.messagesDao = messagesDao;
    }

    @Override
    public void addMessageForUser(Message message) {
        messagesDao.addMessageForUser(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messagesDao.deleteMessage(messageId);
    }

    @Override
    public Collection<Message> getMessagesForUser(UUID userId) {
        return messagesDao.getMessagesForUser(userId);
    }

    @Override
    public void setCheckedFlag(UUID messageId) {
        messagesDao.setCheckedFlag(messageId, true);
    }

    @Override
    public boolean doesMessageIdExists(UUID newMessageId) {
        return !messagesDao.doesMessageIdExists(newMessageId).isEmpty();
    }

    public UUID generateMessageId() {
        UUID randomUUID = UUID.randomUUID();
        while (doesMessageIdExists(randomUUID)) randomUUID = UUID.randomUUID();
        return randomUUID;
    }
}
