package br.com.habittracker.dao;

/** Lançada quando o hábito já tem conclusão na data (regra RN01). */
public class ConclusaoJaExisteException extends Exception {
    public ConclusaoJaExisteException() {
        super("Hábito já concluído nesta data");
    }
}
