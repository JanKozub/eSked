package org.jk.eSked.services.schedule;

import org.jk.eSked.model.ScheduleEntry;

import java.util.Collection;
import java.util.UUID;

public interface ScheduleService {

    Collection<ScheduleEntry> getScheduleEntries(UUID userId);

    void addScheduleEntry(ScheduleEntry scheduleEntry);

    void deleteScheduleEntry(UUID userId, int hour, int day);

    void setScheduleEntries(UUID userId, Collection<ScheduleEntry> entries);

    void deleteScheduleEntries(UUID userId);
}
