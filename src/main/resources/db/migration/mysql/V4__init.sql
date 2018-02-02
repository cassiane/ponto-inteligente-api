CREATE TABLE empresa(
	id bigint(20) not null, 
	cnpj varchar(255) not null,
	data_atualizacao datetime not null,
	data_criacao datetime not null,
	razao_social varchar(255) not null
) ENGINE=InnoDB default charset = utf8;

create table funcionario (
	id bigint(20) not null, 
	cpf varchar(255) not null,
	data_atualizacao datetime not null,
	data_criacao datetime not null,
	email varchar(255) not null,
	nome varchar(255) not null,
	perfil varchar(255) not null,
	qtd_horas_almoco float default null,
	qtd_horas_trabalho_dia float default null,
	senha varchar(255) not null,
	valor_hora decimal(19,2) default null,
	empresa_id bigint(20) default null
) ENGINE=InnoDB default charset = utf8;

create table lancamento (
	id bigint(20) not null, 
	data datetime not null,
	data_atualizacao datetime not null,
	data_criacao datetime not null,
	descricao varchar(255) not null,
	localizacao varchar(255) not null,
	tipo varchar(255) not null,
	funcionario_id bigint(20) default null
) ENGINE=InnoDB default charset = utf8;

--
-- Indexes for table empresa
--
alter table empresa add primary key(id);

--
-- Indexes for table funcionario
--
alter table funcionario add primary key(id), 
 add key fk_empresa_id (empresa_id);

--
-- Indexes for table lancamento
--
alter table lancamento add primary key(id), 
 add key fk_funcionario_id (funcionario_id);
 
--
-- Auto increment for table empresa
--
alter table empresa modify id bigint(20) not null auto_increment;

--
-- Auto increment for table funcionario
--
alter table funcionario modify id bigint(20) not null auto_increment;

--
-- Auto increment for table lancamento
--
alter table lancamento modify id bigint(20) not null auto_increment;

--
-- Constraints for table funcionario
--
alter table funcionario add constraint empresa foreign key(empresa_id) references empresa (id);

--
-- Constraints for table lancamento
--
alter table lancamento add constraint funcionario foreign key(funcionario_id) references funcionario (id);



