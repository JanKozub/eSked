package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> findByUserId(UUID userId);

    @Query("select e from Event e where e.user.id = :id AND e.timestamp >= :startOfWeek AND e.timestamp < :startOfWeek + CAST(7*24*3600 AS LONG) * 1000")
    List<Event> findByStartOfWeek(UUID id, long startOfWeek);

    @Query("select e from Event e where e.user.id = :id AND e.timestamp >= :startOfDay AND e.timestamp < :startOfDay + CAST(24*3600 AS LONG)")
    List<Event> findOnDay(UUID id, long startOfDay);

    @Transactional
    @Modifying
    @Query("update Event e set e.checked_flag = :state where e.id = :id")
    void changeCheckedFlag(UUID id, boolean state);
}
