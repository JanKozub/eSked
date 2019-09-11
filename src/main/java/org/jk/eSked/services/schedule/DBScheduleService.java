package org.jk.eSked.services.schedule;

import org.jk.eSked.dao.ScheduleDao;
import org.jk.eSked.model.ScheduleEntry;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class DBScheduleService implements ScheduleService {

    private final ScheduleDao scheduleDao;

    public DBScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Collection<ScheduleEntry> getScheduleEntries(UUID userId) {
        return scheduleDao.getScheduleEntries(userId);
    }

    @Override
    public void addScheduleEntry(ScheduleEntry scheduleEntry) {
        scheduleDao.persistScheduleEntry(scheduleEntry);
    }

    @Override
    public void deleteScheduleEntry(UUID userId, int hour, int day) {
        scheduleDao.deleteScheduleEntry(userId, hour, day);
    }

    @Override
    public void setScheduleEntries(UUID userId, Collection<ScheduleEntry> entries) {
        for (ScheduleEntry entry : entries) {
            ScheduleEntry scheduleEntry =
                    new ScheduleEntry(userId, entry.getHour(), entry.getDay(), entry.getSubject(),
                            entry.getCreatedDate());
            scheduleDao.persistScheduleEntry(scheduleEntry);
        }
    }

    @Override
    public void deleteScheduleEntries(UUID userId) {
        scheduleDao.deleteScheduleEntries(userId);
    }
}
