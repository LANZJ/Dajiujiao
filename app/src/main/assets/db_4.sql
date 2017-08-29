--通讯录缓存表
--phone_used  1、注册过应用，非好友，2、注册过应用，是好友，3、没有注册过应用
create table phone_contact(
    owner_user_id char(32) not null,
    phone_number char(32) not null,
    phone_name TEXT,
    phone_used smallint,

    primary key (owner_user_id , phone_number)
)
go