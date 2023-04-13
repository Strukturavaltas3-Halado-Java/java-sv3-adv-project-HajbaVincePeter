create table if not exists addresses
(
    id             bigint       not null
    primary key,
    city           varchar(255) null,
    country        varchar(255) null,
    name           varchar(255) null,
    street_address varchar(255) null
    );


create table if not exists irish_addresses
(
    id             bigint       not null
    primary key,
    city           varchar(255) null,
    country        varchar(255) null,
    name           varchar(255) null,
    street_address varchar(255) null,
    county         varchar(255) null
    );

create table if not exists postal_code_addresses
(
    id             bigint       not null
    primary key,
    city           varchar(255) null,
    country        varchar(255) null,
    name           varchar(255) null,
    street_address varchar(255) null,
    postal_code    varchar(255) null
    );

create table if not exists shipments
(
    id              bigint auto_increment
    primary key,
    shipping_date   date         null,
    tracking_number varchar(255) null,
    from_id         bigint       null,
    to_id           bigint       null
    );

create table if not exists packages
(
    id          bigint auto_increment
    primary key,
    weight      int    null,
    shipment_id bigint null,
    foreign key (shipment_id) references shipments (id)
    );