-- Para a implementação de blockchain

create table lancamentoLog (
	id bigint(20) not null,
	data datetime not null,
	data_atualizacao datetime not null,
	data_criacao datetime not null,
	descricao varchar(255) not null,
	tipo varchar(255) not null,
	funcionario_id bigint(20) default null,
	previous_hash varchar(300) not null,
	hash varchar(300) not null,
	operacao varchar(10) not null,
	id_lancamento_alterado bigint(20) default null
) ENGINE=InnoDB default charset = utf8;
alter table lancamentoLog add primary key(id),
add key fk_funcionario_id (funcionario_id);
alter table lancamentoLog
add key fk_lancamento_id (id_lancamento_alterado);
alter table lancamentoLog modify id bigint(20) not null auto_increment;
alter table lancamentoLog add constraint funcionario_id foreign key(funcionario_id) references funcionario (id);
alter table lancamentoLog add constraint lancamento foreign key(id_lancamento_alterado) references lancamento (id);
