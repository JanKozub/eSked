package org.jk.eSked.services.schedule;

import org.jk.eSked.dao.ScheduleDao;
import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public Collection<Entry> getEntries(UUID userId) {
        Collection<ScheduleEntry> scheduleEntries = scheduleDao.getScheduleEntries(userId);
        List<Entry> entries = new ArrayList<>();
        for (ScheduleEntry scheduleEntry : scheduleEntries) {
            Entry entry = new Entry(scheduleEntry.getHour(), scheduleEntry.getDay(), scheduleEntry.getSubject(),
                    scheduleEntry.getCreatedTimestamp());
            entries.add(entry);
        }
        return entries;
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
    public void setScheduleEntries(UUID userId, Collection<Entry> entries) {
        for (Entry entry : entries) {
            ScheduleEntry scheduleEntry =
                    new ScheduleEntry(userId, entry.getHour(), entry.getDay(), entry.getSubject(),
                            entry.getCreatedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
            scheduleDao.persistScheduleEntry(scheduleEntry);
        }
    }

    @Override
    public void deleteScheduleEntries(UUID userId) {
        scheduleDao.deleteScheduleEntries(userId);
    }
}
