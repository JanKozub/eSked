package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.Hour;
import org.jk.esked.app.backend.model.ScheduleEntry;
import org.jk.esked.app.backend.repositories.HourRepository;
import org.jk.esked.app.backend.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class HourService {
    private final HourRepository hourRepository;
    private final ScheduleRepository scheduleRepository;

    public HourService(HourRepository hourRepository, ScheduleRepository scheduleRepository) {
        this.hourRepository = hourRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public void setScheduleHours(Collection<Hour> hours) {
        hourRepository.saveAll(hours);
    }

    public Collection<Hour> getHoursForUser(UUID userId) {
        return hourRepository.findHoursByUserId(userId);
    }

    public Hour getHourByHour(UUID userId, int hour) {
        return hourRepository.getHoursByUserIdAndHour(userId, hour);
    }

    public void deleteAllHourByUserId(UUID userId) {
        hourRepository.deleteAllHoursByUserId(userId);
    }

    public int getScheduleMaxHour(UUID userId) {
        int maxHour = 0;
        for (ScheduleEntry scheduleEntry : scheduleRepository.getScheduleEntriesByUserId(userId)) {
            int checkedHour = scheduleEntry.getHour() + 1;
            if (checkedHour > maxHour) maxHour = checkedHour;
        }
        return maxHour;
    }
}
