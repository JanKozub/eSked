package org.jk.esked.app.backend.repositories;

import org.jk.esked.app.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("select m from Message m where m.user.id = :id")
    List<Message> getMessagesForUser(UUID id);
}
