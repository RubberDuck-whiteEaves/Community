create table USER_INFO
(
    ID           INTEGER auto_increment
        primary key,
    ACCOUNT_ID   CHARACTER VARYING(100),
    NAME         CHARACTER VARYING(50),
    TOKEN        CHARACTER(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT
);
