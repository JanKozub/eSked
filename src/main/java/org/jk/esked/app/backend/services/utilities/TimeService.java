package org.jk.esked.app.backend.services.utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeService {

    public static long localDateToTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();
    }

    public static int timestampToDayOfWeek(long timestamp) {
        return timestampToLocalDateTime(timestamp).getDayOfWeek().getValue();
    }

    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    public static String timestampToFormatedString(long timestamp) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC).format(pattern);
    }

    public static long now() {
        return Instant.now().getEpochSecond();
    }
}
