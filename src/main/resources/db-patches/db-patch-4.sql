drop table Events;

create TABLE Events
(
    creatorId        UUID    NOT NULL,
    eventId          UUID    NOT NULL,
    type             VARCHAR NOT NULL,
    topic            VARCHAR NOT NULL,
    hour             int     NOT NULL,
    timestamp        BIGINT  NOT NULL,
    createdTimestamp BIGINT  NOT NULL
);
