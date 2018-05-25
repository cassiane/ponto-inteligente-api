create table centro_custo (
    id bigint(20) not null,
    centro_custo bigint(20) not null,
    empresa_id bigint(20) default null,
    projeto varchar(255) not null,
    descricao varchar(255) not null
) ENGINE=InnoDB default charset = utf8;

alter table centro_custo add primary key(id);
alter table centro_custo modify id bigint(20) not null auto_increment;
alter table centro_custo add key fk_centro_custo_empresa_id (empresa_id);
alter table centro_custo add constraint empresa_id foreign key(empresa_id) references empresa (id);
