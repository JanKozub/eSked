package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("select m from Message m where m.user.id = :id")
    List<Message> getMessagesForUser(UUID id);

    @Transactional
    @Modifying
    @Query("update Message m set m.checked_flag = :state where m.id = :id")
    void setCheckedFlagByMessageId(UUID id, boolean state);
}
