package org.jk.eSked.backend.model.schedule;

import java.util.UUID;

public record ScheduleHour(UUID userId, int hour, String data) {
}
