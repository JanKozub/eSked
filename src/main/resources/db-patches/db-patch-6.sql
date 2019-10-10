drop table Events;

create TABLE Events (
    userId UUID NOT NULL,
    eventId UUID NOT NULL,
    type VARCHAR NOT NULL,
    topic VARCHAR NOT NULL,
    hour int NOT NULL,
    checkedFlag boolean NOT NULL,
    timestamp BIGINT NOT NULL,
    createdTimestamp BIGINT NOT NULL
);
