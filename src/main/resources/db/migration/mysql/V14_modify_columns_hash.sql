alter table lancamento modify hash varchar(256);
alter table lancamentolog modify hash varchar(256);

alter table lancamento modify previous_hash varchar(256);
alter table lancamentolog modify previous_hash varchar(256);