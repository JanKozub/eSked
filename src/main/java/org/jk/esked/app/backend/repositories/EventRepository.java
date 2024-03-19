package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> findEventByUserId(UUID userId);

    @Query("select e from Event e where e.timestamp >= :startOfWeek AND e.timestamp < :startOfWeek + CAST(7*24*3600 AS LONG) * 1000 AND e.user.id = :id")
    List<Event> findEventByUserIdAndWeek(UUID id, long startOfWeek);

    @Transactional
    @Modifying
    @Query("update Event e set e.checked_flag = :state where e.id = :id")
    void updateEventSetCheckedFlagForEvent(UUID id, boolean state);
}
