package org.jk.eSked.services.hours;

import org.jk.eSked.model.ScheduleHour;

import java.util.Collection;
import java.util.UUID;

public interface HoursService {

    void setScheduleHours(Collection<ScheduleHour> hours);

    Collection<ScheduleHour> getScheduleHours(UUID userId);

    ScheduleHour getScheduleHour(UUID userId, int hour);

    void deleteScheduleHours(UUID userId);

    int getScheduleMaxHour(UUID userId);
}
