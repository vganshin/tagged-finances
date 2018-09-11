create table transactions(
  id serial primary key,
  parent_id integer,
  amount decimal,
  author varchar,
  ts date,
  foreign key (parent_id) references transactions(id)
);

create table tags(
  id serial primary key,
  name varchar
);

create table tags_transactions(
  tag_id integer references tags(id),
  transaction_id integer references transactions(id)
);
