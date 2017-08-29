--会话缓存
CREATE TABLE cw_conversation(
    to_id varchar(32) not null,
    conversation_type smallint not null,
	owner_user_id char(32) not null,
	priority smallint not null default 0,
	ext text null,
	modify_time datetime not null,
	creation_time datetime not null,
	primary key(to_id, owner_user_id)
)
go
--会话消息
create table cw_conversation_message(
    id char(32) not null,
    owner_user_id char(32) not null,
    sender_user_id char(32) not null,
    conversation_type smallint not null,
    conversation_to_id varchar(32) not null,
    message_type smallint not null,
    message_content_type smallint not null,
    content varchar(1000) not null,
    download_url varchar(500) null,
    voice_length integer,
    read_status smallint not null,
    send_status smallint not null,
    ext text null,
    modify_time datetime not null,
    creation_time datetime not null,
    primary key(id, owner_user_id , message_type)
)
go
--用户缓存
create table cw_cache_user(
    user_id char(32) not null PRIMARY KEY,
    user_name varchar(512) null,
    user_logo varchar(1024) null,
    ext text null,
    creation_time datetime null,
    last_update_time datetime null
)
go
--群组缓存
create table cw_cache_group(
    group_id char(32) not null PRIMARY KEY,
    group_name varchar(512) null,
    last_update_time datetime null
)
go