--购物车缓存表
--status含义：1、未选中 2、选中
create table market_goods_car(
    shop_id char(32) not null,
    good_id char(32) not null,
    shop_name varchar(500) not null,
    good_name varchar(500) not null,
    good_icon varchar(500),
    good_count varchar(32),
    good_type varchar(32),
    good_price varchar(32),
    good_market_price varchar(32),
    good_per_box varchar(32),
    good_up_price varchar(32),
    status smallint default 2 not null,
    primary key (good_id)
)
go

alter table goods_car add column good_market_price varchar(32)
go