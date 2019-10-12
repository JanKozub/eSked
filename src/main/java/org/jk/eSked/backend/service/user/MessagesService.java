package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.MessagesDao;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.repositories.MessagesDB;
import org.jk.eSked.ui.components.menu.Menu;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class MessagesService implements MessagesDB {

    private MessagesDao messagesDao;

    public MessagesService(MessagesDao messagesDao) {
        this.messagesDao = messagesDao;
    }

    @Override
    public void addMessageForUser(Message message) {
        messagesDao.addMessageForUser(message);
        Menu.setMessagesBadge(getUncheckedMessages(message.getUserId()).size());
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
        return messagesDao.doesMessageIdExists(newMessageId).size() > 0;
    }

    @Override
    public Collection<Message> getUncheckedMessages(UUID userId) {
        return messagesDao.getUncheckedMessagesForUser(userId, false);
    }

    public UUID generateMessageId() {
        UUID randomUUID = UUID.randomUUID();
        while (doesMessageIdExists(randomUUID)) randomUUID = UUID.randomUUID();
        return randomUUID;
    }
}
