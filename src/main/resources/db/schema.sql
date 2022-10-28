-- 表设计文件
CREATE TABLE `meican_account`
(
    `uid`                     bigint(20)  NOT NULL primary key,
    `account_name`            varchar(64) NOT NULL,
    `account_password`        varchar(64)  NULL,
    `account_cookie`          varchar(256)  NULL,
    `create_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `update_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `deleted`                 int(2)               DEFAULT '0'
);

CREATE TABLE `meican_booking`
(
    `uid`                     bigint(20)  NOT NULL primary key,
    `account_name`            varchar(64) NOT NULL,
    `order_date`              varchar(64) NOT NULL,
    `order_dish`              varchar(64) NOT NULL,
    `order_status`            varchar(64) NOT NULL DEFAULT 'INIT',
    `try_count`               int(2)               DEFAULT '0',
    `error_msg`               varchar(1024) NULL DEFAULT '',
    `create_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `update_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `deleted`                 int(2)               DEFAULT '0'
);

CREATE TABLE `meican_account_dish_check`
(
    `uid`                     bigint(20)  NOT NULL primary key,
    `account_name`            varchar(64) NOT NULL,
    `expire_date`              varchar(64) NOT NULL,
    `create_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `update_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `deleted`                 int(2)               DEFAULT '0'
);
