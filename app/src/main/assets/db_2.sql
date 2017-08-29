--联系人缓存表
create table contact(
    owner_user_id char(32) not null,
    id char(32) not null,
    member_id char(32) not null,
    name varchar(32) ,
    avatar varchar (50),
    primary key (id, member_id , owner_user_id)
)
go