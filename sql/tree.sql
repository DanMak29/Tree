create schema schema_tree;
use schema_tree;
create table tree
(
    id        bigint unsigned auto_increment,
    name      varchar(30) not null,
    left_key  int         not null,
    right_key int         not null,
    level     tinyint unsigned,
    primary key (id)

);

insert into tree(name, left_key, right_key, level)
values ('Комплектующие', '1', '10', '1'),
       ('Процессоры', '2', '7', '2'),
       ('Intel', '3', '4', '3'),
       ('AMD', '5', '6', '3'),
       ('ОЗУ', '8', '9', '2'),
       ('Аудио системы', '11', '16', '1'),
       ('Наушники', '12', '13', '2'),
       ('Колонки', '14', '15', '2');

truncate table tree;



