package br.com.habittracker.dao;

import java.time.LocalDate;

/**
 * O que a regra de conclusão precisa do banco. Ter essa interface permite
 * testar a regra (RN01, RN02, RN05) sem precisar de MySQL.
 */
public interface ConclusaoRepositorio {

    /** O hábito existe e pertence a esse usuário? (RN05) */
    boolean habitoPertenceAoUsuario(int habitoId, int usuarioId);

    /** Já existe conclusão desse hábito nessa data? (RN01) */
    boolean existeConclusao(int habitoId, LocalDate data);

    /** Grava a conclusão associada à data (RN02). Lança exceção se já existir (RN01). */
    void inserir(int habitoId, LocalDate data) throws ConclusaoJaExisteException;
}
