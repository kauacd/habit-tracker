-- Habit Tracker - Incremento 2
-- Execute UMA vez. Só ACRESCENTA a tabela de conclusões: o Incremento 1 continua funcionando.

USE habit_tracker;

CREATE TABLE IF NOT EXISTS conclusoes (
    id        INT       NOT NULL AUTO_INCREMENT,
    habito_id INT       NOT NULL,
    data      DATE      NOT NULL,                 -- RN02: conclusão associada à data
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_conclusao_habito_dia (habito_id, data),   -- RN01: uma conclusão por hábito por dia
    CONSTRAINT fk_conclusoes_habito FOREIGN KEY (habito_id)
        REFERENCES habitos (id) ON DELETE CASCADE
) ENGINE=InnoDB;
