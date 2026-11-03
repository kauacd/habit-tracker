package br.com.habittracker.dao;

/** Lançada quando o e-mail já existe (regra RN03). */
public class EmailJaCadastradoException extends Exception {
    public EmailJaCadastradoException() {
        super("E-mail já cadastrado");
    }
}
