create TABLE DbPatches (
    patch_number INT NOT NULL,
    applied_date BIGINT NOT NULL
);

create TABLE Events (
    userId UUID NOT NULL,
    id UUID NOT NULL,
    timestamp BIGINT NOT NULL,
    hour INT NOT NULL,
    eventType VARCHAR NOT NULL,
    topic VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);

create TABLE ScheduleEntry (
    userId UUID NOT NULL,
    hour INT NOT NULL,
    day INT NOT NULL,
    subject VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);

create TABLE Users (
    id UUID NOT NULL,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    darkTheme boolean NOT NULL,
    scheduleHours boolean NOT NULL,
    email VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    synWGroup boolean NOT NULL,
    createdDate BIGINT NOT NULL,
    lastLoggedDate BIGINT NOT NULL,
    genCode INT NOT NULL,
);

create TABLE Groups (
    isAccepted boolean NOT NULL,
    name VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    leaderId UUID NOT NULL,
    hour INT NOT NULL,
    day INT NOT NULL,
    subject VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);
create TABLE Hours(
    userId UUID NOT NULL,
    hour INT NOT NULL,
    data VARCHAR NOT NULL
);