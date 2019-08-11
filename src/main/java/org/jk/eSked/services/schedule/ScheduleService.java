package org.jk.eSked.services.schedule;

import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.entry.ScheduleEntry;

import java.util.Collection;
import java.util.UUID;

public interface ScheduleService {

    Collection<ScheduleEntry> getScheduleEntries(UUID userId);

    Collection<Entry> getEntries(UUID userId);

    void addScheduleEntry(ScheduleEntry scheduleEntry);

    void deleteScheduleEntry(UUID userId, int hour, int day);

    void setScheduleEntries(UUID userId, Collection<Entry> entries);

    void deleteScheduleEntries(UUID userId);
}
