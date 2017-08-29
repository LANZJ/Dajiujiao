--以--开头的行为注释行,语句间以go隔开(注意：这不是标准的sql格式，如果--写在一行中间将会被当做sql，--必须独立成行,go也必须独立成行)
--please pay attention to above words！！！UTF-8 coding
--每次升级请不要在原来的db_*.sql中修改，而是新创建一个文件，然后把数据库版本号+1

--购物车缓存表
--status含义：1、未选中 2、选中
create table goods_car(
    shop_id char(32) not null,
    good_id char(32) not null,
    shop_name varchar(500) not null,
    good_name varchar(500) not null,
    good_icon varchar(500),
    good_count varchar(32),
    good_type varchar(32),
    good_price varchar(32),
    good_per_box varchar(32),
    good_up_price varchar(32),
    status smallint default 1 not null,
    primary key (good_id)
)
go

CREATE UNIQUE INDEX unique_index_id ON goods_car(good_id);
go

--圈子缓存表
--state含义:1发送中2发送成功3发送失败
create table circle(
    id char(32) not null,
    owner_user_id char(32) not null,
    creationTime datetime not null,
    state smallint default 2 not null,
    data TEXT,
    primary key (id, owner_user_id)
)
go

--联系人缓存表
create table address_user(
    owner_user_id char(32) not null,
    user_id char(32) not null,
    name varchar(32) ,
    avatar varchar (50),
    primary key (user_id, owner_user_id)
)
go

--地址缓存表
--selected含义:1选中 2未选中
create table address(
    owner_user_id char(32) not null,
    id char(32) not null ,
    receiver varchar(32),
    phone varchar(32),
    area varchar(50),
    selected smallint default 2 not null,
    detail_address TEXT
)
go

CREATE UNIQUE INDEX index_id ON address(id);
go

--全国省市区缓存表
create table area(
    code varchar(32) not null,

    big_area TEXT
)
go

--登录用户店铺列表缓存表
--follow字段含义， 1、收藏 ， 2、未收藏
create table shops(
    id char(32) not null,
    owner_user_id char(32) not null,
    followed smallint default 2,
    data TEXT,
    primary key (id, owner_user_id)
)
go

--未登录用户店铺列表缓存表
create table un_login_shop(
    id char(32) not null,
    data TEXT,
    primary key(id)
)
go

--老用户缓存表
create table customer(
    id char(32) not null,
    owner_user_id char(32) not null,
    data TEXT,
    primary key(id , owner_user_id)
)
go

--我的红酒缓存表
create table my_wine(
    id char(32) not null,
    owner_user_id char(32) not null,
    data TEXT,
    primary key(owner_user_id , id)
)
go
