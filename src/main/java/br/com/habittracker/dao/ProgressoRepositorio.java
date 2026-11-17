package br.com.habittracker.dao;

import br.com.habittracker.model.Habito;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/** O que histórico e progresso precisam do banco (permite testar as regras sem MySQL). */
public interface ProgressoRepositorio {

    /** O hábito, se for desse usuário; senão null (RN05). */
    Habito buscarHabitoDoUsuario(int habitoId, int usuarioId);

    /** Datas em que o hábito foi concluído, entre as duas datas (inclusive). */
    Set<LocalDate> datasConcluidas(int habitoId, LocalDate de, LocalDate ate);

    /** Quantas conclusões o hábito já teve, em qualquer data. */
    int totalConclusoes(int habitoId);

    /** Datas concluídas de TODOS os hábitos do usuário no período, por id de hábito. */
    Map<Integer, Set<LocalDate>> datasConcluidasPorHabito(int usuarioId, LocalDate de, LocalDate ate);

    /** Quantos hábitos do usuário foram concluídos em cada dia do período. */
    Map<LocalDate, Integer> concluidasPorDia(int usuarioId, LocalDate de, LocalDate ate);

    int contarHabitos(int usuarioId);
}
