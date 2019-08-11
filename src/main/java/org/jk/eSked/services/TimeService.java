package org.jk.eSked.services;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;

@Service
public class TimeService {

    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public LocalDate firstDayOfWeek(LocalDate referenceDate) {
        TemporalAdjuster adjuster = DayOfWeek.MONDAY;
        return referenceDate.with(adjuster);
    }
}
