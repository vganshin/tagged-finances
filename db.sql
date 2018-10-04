create table transactions(
  id serial primary key,
  parent_id integer,
  amount decimal,
  author varchar,
  creation_ts date,
  deposit_id integer,
  foreign key (parent_id) references transactions(id)
  foreign key (deposit_id) references deposits(id)
);

create table deposits(
  id serial primary key,
  name varchar,
  balance decimal
);

create table tags(
  id serial primary key,
  name varchar
);

create table tags_transactions(
  tag_id integer references tags(id),
  transaction_id integer references transactions(id)
);


insert into tags(name) values('лента'),('продукты'),('фрукты'),('молочка');
-- postgres=# select * from tags;
--  id |   name
-- ----+----------
--   1 | лента
--   2 | продукты
--   3 | фрукты
--   4 | молочка
-- (4 rows)

-- 1500 лента
insert into transactions(parent_id, amount) values(null, 1500);
insert into tags_transactions values(1, 1);

-- 1000 продукты
insert into transactions(parent_id, amount) values(1, 1000);
insert into tags_transactions values(2, 2);

-- 300 фрукты
insert into transactions(parent_id, amount) values(2, 300);
insert into tags_transactions values(3, 3);

-- 70 молочка
insert into transactions(parent_id, amount) values(2, 70);
insert into tags_transactions values(4, 4);

-- добавляем еще тэг на рутовую транзакцию
insert into tags(name) values('магазин');
insert into tags_transactions values(5, 1);


with recursive trans(id, parent_id)
as (
  select id, parent_id from transactions where id = 4
  union
  select t.id, t.parent_id from transactions as t
    join trans on trans.parent_id = t.id
)
select tags.name  from trans as t
  join tags_transactions as tt on t.id = tt.transaction_id
  join tags on tt.tag_id = tags.id;
