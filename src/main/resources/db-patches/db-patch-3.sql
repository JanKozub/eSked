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
    verified boolean NOT NULL,
);