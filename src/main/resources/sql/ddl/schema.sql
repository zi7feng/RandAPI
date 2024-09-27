CREATE DATABASE IF NOT EXISTS RANDAPI;

USE RANDAPI;

-- user table
create table if not exists RANDAPI.User
(
    id             bigint auto_increment comment 'id' primary key,
    user_account    varchar(256)                           not null comment 'User Account',
    user_password   varchar(512)                           null comment 'user password',
    balance        bigint       default 30                not null comment 'user balance, 30 by default on signing up',
    access_key VARCHAR(512)  null COMMENT 'access key',
    secret_key VARCHAR(512)  NULL COMMENT 'secret key',
    constraint uni_userAccount
        unique (user_account)
)
    comment 'User table';