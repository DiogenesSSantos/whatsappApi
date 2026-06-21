

CREATE TABLE IF NOT EXISTS tb_paciente (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(36) NOT NULL UNIQUE,
  nome_paciente VARCHAR(255),
  bairro VARCHAR(255),
  consulta_nome VARCHAR(255),
  data_atendimento DATETIME,
  data_marcacao DATETIME,
  status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS paciente_telefones (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  paciente_id BIGINT NOT NULL,
  celular VARCHAR(32),
  is_whatsapp BOOLEAN,
  CONSTRAINT fk_paciente_telefones_paciente FOREIGN KEY (paciente_id)
    REFERENCES tb_paciente(id) ON DELETE CASCADE
);

CREATE INDEX idx_paciente_codigo ON tb_paciente(codigo);
CREATE INDEX idx_telefones_paciente_id ON paciente_telefones(paciente_id);
