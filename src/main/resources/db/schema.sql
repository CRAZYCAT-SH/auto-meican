#表设计文件
drop table if exists `meican_task`;
CREATE TABLE `meican_task`
(
    `uid`                     bigint(20)  NOT NULL primary key,
    `meican_account_name`     varchar(64) NOT NULL,
    `meican_account_password` varchar(64) NOT NULL,
    `order_name`              varchar(64) NOT NULL,
    `order_date`              varchar(64) NOT NULL,
    `order_dish`              varchar(64) NOT NULL,
    `order_status`            varchar(64) NOT NULL DEFAULT 'INIT',
    `create_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `update_date`             timestamp   NULL     DEFAULT CURRENT_TIMESTAMP,
    `deleted`                 int(2)               DEFAULT '0'
);