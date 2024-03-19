package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.ScheduleEntry;
import org.jk.esked.app.backend.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<ScheduleEntry> getScheduleEntriesByUserId(UUID userId) {
        return scheduleRepository.getScheduleEntriesByUserId(userId);
    }

    public void saveScheduleEntry(ScheduleEntry scheduleEntry) {
        scheduleRepository.save(scheduleEntry);
    }

    public void deleteScheduleEntry(UUID userId, int hour, int day) {
        scheduleRepository.deleteScheduleEntryByUserAndHourAndDay(userId, hour, day);
    }

    public void deleteAllScheduleEntriesForId(UUID userId) {
        scheduleRepository.deleteAllScheduleEntriesForId(userId);
    }

    public boolean doesUserHasEntries(UUID userId) {
        return !scheduleRepository.getScheduleEntriesByUserId(userId).isEmpty();
    }

    public void setScheduleEntries(UUID userId, Collection<ScheduleEntry> entries) {
        for (ScheduleEntry entry : entries) {
            entry.getUser().setId(userId);
            scheduleRepository.save(entry);
        }
    }
}
