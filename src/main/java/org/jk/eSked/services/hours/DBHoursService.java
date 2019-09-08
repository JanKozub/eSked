package org.jk.eSked.services.hours;

import org.jk.eSked.dao.HoursDao;
import org.jk.eSked.dao.ScheduleDao;
import org.jk.eSked.model.ScheduleHour;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class DBHoursService implements HoursService {

    private final HoursDao hoursDao;
    private final ScheduleDao scheduleDao;

    public DBHoursService(HoursDao hoursDao, ScheduleDao scheduleDao) {
        this.hoursDao = hoursDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public void setScheduleHours(Collection<ScheduleHour> hours) {
        for (ScheduleHour hour : hours) {
            hoursDao.addHour(hour);
        }
    }

    @Override
    public Collection<ScheduleHour> getHours(UUID userId) {
        return hoursDao.getHours(userId);
    }

    @Override
    public ScheduleHour getScheduleHour(UUID userId, int hour) {
        return hoursDao.getHour(userId, hour);
    }

    @Override
    public void deleteScheduleHours(UUID userId) {
        hoursDao.deleteHours(userId);
    }

    @Override
    public int getScheduleMaxHour(UUID userId) {
        int maxHour = 0;
        for (ScheduleEntry scheduleEntry : scheduleDao.getScheduleEntries(userId)) {
            int checkedHour = scheduleEntry.getHour() + 1;
            if (checkedHour > maxHour) maxHour = checkedHour;
        }
        return maxHour;
    }
}
