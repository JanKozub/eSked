package org.jk.eSked.backend.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeService {

    public static long localDateToInstant(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDate instantToLocalDate(long timestamp) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    public static String instantToFormattedDate(long timestamp) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(pattern);
    }

    public static long now() {
        return Instant.now().toEpochMilli();
    }
}
