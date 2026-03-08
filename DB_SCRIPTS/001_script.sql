create table if not exists public.accounts
(
    id              uuid             not null,
    account_number  varchar(255)     not null,
    account_type    smallint         not null,
    created_at      timestamp(6),
    initial_balance double precision not null,
    person_id       uuid             not null,
    status          boolean          not null
);

alter table public.accounts
    owner to postgres;

alter table public.accounts
    add primary key (id);

alter table public.accounts
    add constraint uk_6kplolsdtr3slnvx97xsy2kc8
        unique (account_number);

alter table public.accounts
    add constraint accounts_account_type_check
        check ((account_type >= 0) AND (account_type <= 1));

create table if not exists public.movements
(
    id                uuid             not null,
    account_number    varchar(255),
    created_at        timestamp(6),
    initial_balance   double precision not null,
    status            boolean,
    detail            varchar(255),
    transaction_type  varchar(255)     not null,
    transaction_value double precision not null
);

alter table public.movements
    owner to postgres;

alter table public.movements
    add primary key (id);

create table if not exists public.persons
(
    dtype            varchar(31) not null,
    id               uuid        not null,
    address          varchar(255),
    age              integer     not null,
    email_identifier varchar(255),
    gender           varchar(255),
    name             varchar(255),
    phone_number     varchar(255),
    client_id        varchar(255),
    created_at       timestamp(6),
    password         varchar(255),
    status           varchar(255)
);

alter table public.persons
    owner to postgres;

alter table public.persons
    add primary key (id);


