--个人动态缓存表
create table user_circle(
    id char(32) not null,
    user_id char(32) not null,
    creationTime datetime not null,
    data TEXT,
    primary key (id, user_id)
)
go