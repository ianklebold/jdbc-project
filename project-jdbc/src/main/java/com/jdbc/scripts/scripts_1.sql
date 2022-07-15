drop table if exists person;

create table person(
    id int auto_increment primary key,
    name varchar(100) not null,
    lastname varchar(100) not null,
    nickname varchar(100)
);