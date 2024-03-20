package org.jk.esked.app.backend.services;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeService {

    public static long localDateToInstant(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();
    }

    public static LocalDateTime instantToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    public static String instantToFormattedDate(long timestamp) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC).format(pattern);
    }

    public static long now() {
        return Instant.now().getEpochSecond();
    }
}
