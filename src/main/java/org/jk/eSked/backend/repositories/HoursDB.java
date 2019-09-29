package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.ScheduleHour;

import java.util.Collection;
import java.util.UUID;

public interface HoursDB {

    void setScheduleHours(Collection<ScheduleHour> hours);

    Collection<ScheduleHour> getHours(UUID userId);

    ScheduleHour getScheduleHour(UUID userId, int hour);

    void deleteScheduleHours(UUID userId);

    int getScheduleMaxHour(UUID userId);
}
