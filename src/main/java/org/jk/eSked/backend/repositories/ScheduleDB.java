package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.schedule.ScheduleEntry;

import java.util.Collection;
import java.util.UUID;

public interface ScheduleDB {

    Collection<ScheduleEntry> getScheduleEntries(UUID userId);

    void addScheduleEntry(ScheduleEntry scheduleEntry);

    void deleteScheduleEntry(UUID userId, int hour, int day);

    void setScheduleEntries(UUID userId, Collection<ScheduleEntry> entries);

    void deleteScheduleEntries(UUID userId);
}
