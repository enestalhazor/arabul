create table products (
    id serial primary key,
    name varchar(255) not null,
    description varchar(1000) not null,
    photo varchar(255) not null,
    price float8 not null,
    category varchar(255) not null
);

create table public.users (
    id serial primary key,
    name varchar(100) not null,
    email varchar(150) not null unique,
    phone varchar(20) unique,
    password varchar(255) not null,
    address varchar(255) not null,
    profile_picture varchar(255)
);

insert into products (name, description, photo, price, category) VALUES ('hikayeler', 'hikaye', 'hikaye.jpg', 123.50, 'book');
insert into products (name, description, photo, price, category) VALUES ('masallar', 'masal', 'masal.jpg', 123.50, 'book');
insert into users (name, email, phone, password, address, profile_picture) VALUES ('enes', 'enes@gmail.com', '5519710453', '12345', 'ankara/etimesgut', 'enes.jpeg');