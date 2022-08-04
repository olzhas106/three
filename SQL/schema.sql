create table nested_sets

(
    id     serial8,
    name   varchar not null,
    "left" int8    not null,
    level  int8    not null,
    "right"  int8    not null,
    primary key (id)
);