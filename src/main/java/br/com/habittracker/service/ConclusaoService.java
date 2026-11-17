package br.com.habittracker.service;

import br.com.habittracker.dao.ConclusaoJaExisteException;
import br.com.habittracker.dao.ConclusaoRepositorio;

import java.time.LocalDate;

/** US03 - Marcar hábito como concluído. Regras RN01, RN02 e RN05. */
public class ConclusaoService {

    public enum Resultado { CONCLUIDO, JA_CONCLUIDO, NAO_ENCONTRADO }

    private final ConclusaoRepositorio repositorio;

    public ConclusaoService(ConclusaoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Resultado concluir(int usuarioId, int habitoId, LocalDate hoje) {
        // RN05: só quem é dono do hábito pode concluí-lo
        if (!repositorio.habitoPertenceAoUsuario(habitoId, usuarioId)) {
            return Resultado.NAO_ENCONTRADO;
        }
        // RN01: no máximo uma conclusão por hábito por dia
        if (repositorio.existeConclusao(habitoId, hoje)) {
            return Resultado.JA_CONCLUIDO;
        }
        try {
            repositorio.inserir(habitoId, hoje); // RN02: registra associada à data
            return Resultado.CONCLUIDO;
        } catch (ConclusaoJaExisteException e) {
            // duas requisições ao mesmo tempo: o banco (UNIQUE) é a última barreira
            return Resultado.JA_CONCLUIDO;
        }
    }
}
