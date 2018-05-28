insert into lancamento (data, data_atualizacao, data_criacao, descricao, tipo, funcionario_id,
previous_hash, hash, ativo) values (now(), now(), now(), 'registro_inicial', 'REGISTRO_INICIAL',
(SELECT `id` FROM `funcionario` WHERE `cpf` = '16248890935'), '0', '0', 1);