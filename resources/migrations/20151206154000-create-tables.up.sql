
create table if not exists deposits(
  id serial primary key,
  name varchar,
  balance decimal
);

create table if not exists transactions(
  id serial primary key,
  parent_id integer,
  amount decimal,
  author varchar,
  creation_ts date,
  deposit_id integer,
  tags varchar,
  foreign key (parent_id) references transactions(id),
  foreign key (deposit_id) references deposits(id)
);





create table if not exists tags(
  id serial primary key,
  name varchar 
);


create table if not exists tags_transactions(
  tag_id integer references tags(id),
  transaction_id integer references transactions(id)
);


-- insert into tags(name) values('лента'),('продукты'),('фрукты'),('молочка');

-- select * from tags;


-- insert into transactions(parent_id, amount) values(:parent_id, :amount);


-- select * from transactions where parent like :parent-like;


-- insert into tags_transactions values(1, 1);

 
-- insert into tags(name) values(:name);



-- -- :name select-trans-like :? :1
-- with recursive trans(id, parent_id)
-- as (
--   select id, parent_id from transactions where id = 4
--   union
--   select t.id, t.parent_id from transactions as t
--     join trans on trans.parent_id = t.id
-- )

-- -- :name select-tags-name :!
-- select tags.name  from trans as t
--   join tags_transactions as tt on t.id = tt.transaction_id
--   join tags on tt.tag_id = tags.id;