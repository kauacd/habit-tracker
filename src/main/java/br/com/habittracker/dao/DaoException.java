package br.com.habittracker.dao;

/** Erro inesperado de acesso ao banco (conexão, SQL, etc.). */
public class DaoException extends RuntimeException {
    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
