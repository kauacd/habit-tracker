-- Habit Tracker - Incremento 1
-- Execute uma vez antes de subir a aplicação.

CREATE DATABASE IF NOT EXISTS habit_tracker
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE habit_tracker;

CREATE TABLE IF NOT EXISTS usuarios (
    id         INT          NOT NULL AUTO_INCREMENT,
    nome       VARCHAR(80)  NOT NULL,
    email      VARCHAR(120) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    criado_em  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_email (email)          -- RN03: e-mail único
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS habitos (
    id         INT          NOT NULL AUTO_INCREMENT,
    usuario_id INT          NOT NULL,
    nome       VARCHAR(80)  NOT NULL,
    descricao  VARCHAR(255) NOT NULL,
    criado_em  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_habitos_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id) ON DELETE CASCADE   -- RN05: hábito pertence a um usuário
) ENGINE=InnoDB;
