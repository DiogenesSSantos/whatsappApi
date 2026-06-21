
SET @@session.time_zone = '+00:00';

-- 1
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'João Silva', 'Centro', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 2 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999010101', TRUE);

-- 2
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Maria Souza', 'Boa Vista', 'Retorno', DATE_ADD(NOW(), INTERVAL 5 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999020102', TRUE);
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '552733330102', FALSE);

-- 3
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Ana Oliveira', 'Jardim', 'Vacinação', DATE_ADD(NOW(), INTERVAL 1 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999030103', TRUE);

-- 4
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Carlos Pereira', 'Vila Nova', 'Exame', DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999040104', FALSE);

-- 5
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Mariana Costa', 'São José', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 3 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999050105', TRUE);

-- 6
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Pedro Almeida', 'Nossa Senhora', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 4 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999060106', TRUE);

-- 7
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Beatriz Fernandes', 'Recanto', 'Retorno', DATE_ADD(NOW(), INTERVAL 6 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999070107', FALSE);

-- 8
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Lucas Martins', 'Alegria', 'Vacinação', DATE_ADD(NOW(), INTERVAL 8 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999080108', TRUE);

-- 9
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Fernanda Rocha', 'Bela Vista', 'Exame', DATE_ADD(NOW(), INTERVAL 9 DAY), NOW(), 'REJEITADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999090109', FALSE);

-- 10
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Rafael Lima', 'Cidade Alta', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 10 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999100110', TRUE);

-- 11
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Patrícia Gomes', 'Centro', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 11 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999110111', TRUE);

-- 12
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Thiago Santos', 'Boa Vista', 'Retorno', DATE_ADD(NOW(), INTERVAL 12 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999120112', FALSE);

-- 13
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Aline Ribeiro', 'Jardim', 'Vacinação', DATE_ADD(NOW(), INTERVAL 13 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999130113', TRUE);

-- 14
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Gustavo Nunes', 'Vila Nova', 'Exame', DATE_ADD(NOW(), INTERVAL 14 DAY), NOW(), 'NAO_POSSUI_WHATSAPP');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999140114', FALSE);

-- 15
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Carolina Castro', 'São José', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 15 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999150115', TRUE);

-- 16
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Bruno Rodrigues', 'Nossa Senhora', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 16 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999160116', TRUE);

-- 17
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Larissa Almeida', 'Recanto', 'Retorno', DATE_ADD(NOW(), INTERVAL 17 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999170117', FALSE);

-- 18
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Mateus Barros', 'Alegria', 'Vacinação', DATE_ADD(NOW(), INTERVAL 18 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999180118', TRUE);

-- 19
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Sofia Menezes', 'Bela Vista', 'Exame', DATE_ADD(NOW(), INTERVAL 19 DAY), NOW(), 'REJEITADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999190119', FALSE);

-- 20
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Eduardo Carvalho', 'Cidade Alta', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 20 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999200120', TRUE);

-- 21
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Isabela Martins', 'Centro', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 21 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999210121', TRUE);

-- 22
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Felipe Dias', 'Boa Vista', 'Retorno', DATE_ADD(NOW(), INTERVAL 22 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999220122', FALSE);

-- 23
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Marina Pinto', 'Jardim', 'Vacinação', DATE_ADD(NOW(), INTERVAL 23 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999230123', TRUE);

-- 24
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'André Moreira', 'Vila Nova', 'Exame', DATE_ADD(NOW(), INTERVAL 24 DAY), NOW(), 'NAO_POSSUI_WHATSAPP');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999240124', FALSE);

-- 25
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Juliana Teixeira', 'São José', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 25 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999250125', TRUE);

-- 26
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Ricardo Lopes', 'Nossa Senhora', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 26 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999260126', TRUE);

-- 27
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Bianca Freitas', 'Recanto', 'Retorno', DATE_ADD(NOW(), INTERVAL 27 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999270127', FALSE);

-- 28
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Guilherme Rocha', 'Alegria', 'Vacinação', DATE_ADD(NOW(), INTERVAL 28 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999280128', TRUE);

-- 29
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Camila Azevedo', 'Bela Vista', 'Exame', DATE_ADD(NOW(), INTERVAL 29 DAY), NOW(), 'REJEITADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999290129', FALSE);

-- 30
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Vitor Carvalho', 'Cidade Alta', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999300130', TRUE);

-- 31
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Renata Sousa', 'Centro', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 4 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999310131', TRUE);

-- 32
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Diego Fernandes', 'Boa Vista', 'Retorno', DATE_ADD(NOW(), INTERVAL 6 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999320132', FALSE);

-- 33
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Paula Nascimento', 'Jardim', 'Vacinação', DATE_ADD(NOW(), INTERVAL 8 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999330133', TRUE);

-- 34
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Rodrigo Pinto', 'Vila Nova', 'Exame', DATE_ADD(NOW(), INTERVAL 10 DAY), NOW(), 'NAO_POSSUI_WHATSAPP');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999340134', FALSE);

-- 35
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Sabrina Lopes', 'São José', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 12 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999350135', TRUE);

-- 36
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Marcelo Teixeira', 'Nossa Senhora', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 14 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999360136', TRUE);

-- 37
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Natália Cardoso', 'Recanto', 'Retorno', DATE_ADD(NOW(), INTERVAL 16 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999370137', FALSE);

-- 38
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Igor Mendes', 'Alegria', 'Vacinação', DATE_ADD(NOW(), INTERVAL 18 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999380138', TRUE);

-- 39
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Lívia Rocha', 'Bela Vista', 'Exame', DATE_ADD(NOW(), INTERVAL 20 DAY), NOW(), 'REJEITADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999390139', FALSE);

-- 40
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Fábio Gomes', 'Cidade Alta', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 22 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999400140', TRUE);

-- 41
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Daniela Ribeiro', 'Centro', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 2 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999410141', TRUE);

-- 42
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Bruno Silva', 'Boa Vista', 'Retorno', DATE_ADD(NOW(), INTERVAL 3 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999420142', FALSE);

-- 43
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Priscila Santos', 'Jardim', 'Vacinação', DATE_ADD(NOW(), INTERVAL 4 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999430143', TRUE);

-- 44
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Alexandre Nogueira', 'Vila Nova', 'Exame', DATE_ADD(NOW(), INTERVAL 5 DAY), NOW(), 'NAO_POSSUI_WHATSAPP');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999440144', FALSE);

-- 45
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Simone Carvalho', 'São José', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 6 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999450145', TRUE);

-- 46
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Leandro Vieira', 'Nossa Senhora', 'Consulta Geral', DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999460146', TRUE);

-- 47
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Tatiana Lopes', 'Recanto', 'Retorno', DATE_ADD(NOW(), INTERVAL 8 DAY), NOW(), 'AGUARDANDO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999470147', FALSE);

-- 48
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Hugo Fernandes', 'Alegria', 'Vacinação', DATE_ADD(NOW(), INTERVAL 9 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999480148', TRUE);

-- 49
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Aline Moreira', 'Bela Vista', 'Exame', DATE_ADD(NOW(), INTERVAL 10 DAY), NOW(), 'REJEITADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999490149', FALSE);

-- 50
INSERT INTO tb_paciente (codigo, nome_paciente, bairro, consulta_nome, data_atendimento, data_marcacao, status)
VALUES (UUID(), 'Mateus Rocha', 'Cidade Alta', 'Acompanhamento', DATE_ADD(NOW(), INTERVAL 11 DAY), NOW(), 'MARCADO');
SET @id = LAST_INSERT_ID();
INSERT INTO paciente_telefones (paciente_id, celular, is_whatsapp) VALUES (@id, '5527999500150', TRUE);
