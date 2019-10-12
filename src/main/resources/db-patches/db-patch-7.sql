create TABLE Messages (
    userId UUID NOT NULL,
    timestamp BIGINT NOT NULL,
    text VARCHAR NOT NULL,
    checkedFlag boolean NOT NULL,
);
