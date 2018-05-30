alter table lancamento add column centro_custo_id bigint(20);
alter table lancamento add key fk_centro_custo_id (centro_custo_id);
alter table lancamento add constraint centro_custo foreign key(centro_custo_id) references centro_custo (id);