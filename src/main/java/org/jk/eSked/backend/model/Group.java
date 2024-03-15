package org.jk.eSked.backend.model;

import java.util.UUID;

public record Group(String name, int groupCode, UUID leaderId, boolean isAccepted, long createdDate) {
}
