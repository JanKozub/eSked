package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<ScheduleEntry, UUID> {

    @Query("select e from ScheduleEntry e where e.user.id = :id")
    List<ScheduleEntry> findByUserId(UUID id);

    @Query("select e from ScheduleEntry e where e.user.id = :id and e.day = :day and e.hour = :hour")
    List<ScheduleEntry> findByUserIdAndDayAndHour(UUID id, int day, int hour);

    @Transactional
    @Modifying
    @Query("delete from ScheduleEntry e where e.id = :id")
    void deleteAllScheduleEntriesForId(UUID id);

    @Transactional
    @Modifying
    @Query("delete from ScheduleEntry e where e.user.id = :id and e.hour = :hour and e.day = :day")
    void deleteScheduleEntryByUserAndHourAndDay(UUID id, int hour, int day);
}
