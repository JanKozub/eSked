package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.Message;

import java.util.Collection;
import java.util.UUID;

public interface MessagesDB {

    void addMessageForUser(Message message);

    void deleteMessage(UUID messageId);

    Collection<Message> getMessagesForUser(UUID userId);

    void setCheckedFlag(UUID messageId);

    boolean doesMessageIdExists(UUID newMessageId);

}
