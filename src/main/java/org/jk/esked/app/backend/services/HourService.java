package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.Hour;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.repositories.HourRepository;
import org.jk.esked.app.backend.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class HourService {
    private final HourRepository hourRepository;
    private final ScheduleRepository scheduleRepository;

    public HourService(HourRepository hourRepository, ScheduleRepository scheduleRepository) {
        this.hourRepository = hourRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public void saveHour(Hour hour) {
        hourRepository.save(hour);
    }

    public void setScheduleHours(Collection<Hour> hours) {
        hourRepository.saveAll(hours);
    }

    public List<Hour> getHourByUserId(UUID userId) {
        return hourRepository.findHoursByUserId(userId);
    }

    public Hour getHourValueByHour(UUID userId, int hour) {
        return hourRepository.getHoursByUserIdAndHour(userId, hour);
    }

    public void deleteHourByUserIdAndHour(UUID userId, int hour) {
        hourRepository.deleteHourByUserIdAndHour(userId, hour);
    }

    public void deleteAllHourByUserId(UUID userId) {
        hourRepository.deleteAllHoursByUserId(userId);
    }

    public int getScheduleMaxHour(UUID userId) {
        int maxHour = 0;
        for (ScheduleEntry scheduleEntry : scheduleRepository.findByUserId(userId)) {
            if (scheduleEntry.getHour() > maxHour)
                maxHour = scheduleEntry.getHour();
        }
        return maxHour;
    }
}
