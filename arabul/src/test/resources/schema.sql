create table products (
    id serial primary key,
    name varchar(255) not null,
    description varchar(1000) not null,
    photo varchar(255) not null,
    price float8 not null,
    category varchar(255) not null
);

insert into products (name, description, photo, price, category) VALUES ('hikayeler', 'hikaye', 'hikaye.jpg', 123.50, 'book');
insert into products (name, description, photo, price, category) VALUES ('masallar', 'masal', 'masal.jpg', 123.50, 'book');
