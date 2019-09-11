drop table Groups;

create TABLE Groups (
    name VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    leaderId UUID NOT NULL,
    isAccepted boolean NOT NULL,
    createdDate BIGINT NOT NULL
);

drop table Users;

create TABLE Users (
    id UUID NOT NULL,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    darkTheme boolean NOT NULL,
    scheduleHours boolean NOT NULL,
    email VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    eventsSyn boolean NOT NULL,
    tableSyn boolean NOT NULL,
    createdDate BIGINT NOT NULL,
    lastLoggedDate BIGINT NOT NULL,
    genCode INT NOT NULL,
);