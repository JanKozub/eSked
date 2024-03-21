package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleEntryService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleEntryService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<ScheduleEntry> getAllByUserId(UUID userId) {
        return scheduleRepository.findByUserId(userId);
    }

    public void save(ScheduleEntry scheduleEntry) {
        scheduleRepository.save(scheduleEntry);
    }

    public void delete(UUID userId, int hour, int day) {
        scheduleRepository.deleteScheduleEntryByUserAndHourAndDay(userId, hour, day);
    }

    public void deleteAllByUserId(UUID userId) {
        scheduleRepository.deleteAllScheduleEntriesForId(userId);
    }

    public void setAllByUserId(UUID userId, List<ScheduleEntry> entries) {
        for (ScheduleEntry entry : entries) {
            entry.getUser().setId(userId);
            scheduleRepository.save(entry);
        }
    }

    public ScheduleEntry findByUserIdAndDayAndHour(UUID id, int day, int hour) {
        List<ScheduleEntry> entries = scheduleRepository.findByUserIdAndDayAndHour(id, day, hour);
        if (entries.isEmpty()) return null;

        return entries.get(0);
    }
}
