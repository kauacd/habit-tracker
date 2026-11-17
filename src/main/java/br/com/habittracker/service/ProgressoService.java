package br.com.habittracker.service;

import br.com.habittracker.dao.ProgressoRepositorio;
import br.com.habittracker.model.Habito;
import br.com.habittracker.util.Datas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** US04 (histórico individual) e US05 (progresso semanal). */
public class ProgressoService {

    private final ProgressoRepositorio repositorio;

    public ProgressoService(ProgressoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /** Resumo dos últimos 7 dias de cada hábito (usado nos cartões de "Meus Hábitos"). */
    public Map<Integer, Resumo7Dias> resumosPorHabito(int usuarioId, List<Integer> habitoIds, LocalDate hoje) {
        Map<Integer, Set<LocalDate>> datas =
                repositorio.datasConcluidasPorHabito(usuarioId, hoje.minusDays(Resumo7Dias.DIAS - 1), hoje);
        Map<Integer, Resumo7Dias> resumos = new HashMap<>();
        for (int id : habitoIds) {
            Set<LocalDate> doHabito = datas.containsKey(id) ? datas.get(id) : Collections.<LocalDate>emptySet();
            resumos.put(id, montarResumo(doHabito, hoje));
        }
        return resumos;
    }

    /** Histórico de um hábito, ou null se o hábito não for do usuário (RN05). */
    public HistoricoHabito historico(int usuarioId, int habitoId, LocalDate hoje) {
        Habito habito = repositorio.buscarHabitoDoUsuario(habitoId, usuarioId);
        if (habito == null) {
            return null;
        }
        Set<LocalDate> datas = repositorio.datasConcluidas(habitoId, hoje.minusDays(Resumo7Dias.DIAS - 1), hoje);
        return new HistoricoHabito(habito.getId(), habito.getNome(), habito.getDescricao(),
                montarResumo(datas, hoje), repositorio.totalConclusoes(habitoId));
    }

    /** A semana atual (segunda a domingo). Dias que ainda não chegaram ficam vazios. */
    public ProgressoSemanal progressoSemanal(int usuarioId, LocalDate hoje) {
        LocalDate inicio = Datas.inicioDaSemana(hoje);
        LocalDate fim = inicio.plusDays(6);
        int totalHabitos = repositorio.contarHabitos(usuarioId);
        Map<LocalDate, Integer> porDia = repositorio.concluidasPorDia(usuarioId, inicio, fim);

        List<DiaSemana> dias = new ArrayList<>();
        int soma = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate data = inicio.plusDays(i);
            boolean futuro = data.isAfter(hoje);
            int concluidas = (futuro || !porDia.containsKey(data)) ? 0 : porDia.get(data);
            soma += concluidas;
            dias.add(new DiaSemana(data, concluidas, totalHabitos, futuro, data.equals(hoje)));
        }
        return new ProgressoSemanal(inicio, fim, dias, totalHabitos, soma);
    }

    static Resumo7Dias montarResumo(Set<LocalDate> concluidas, LocalDate hoje) {
        List<DiaHabito> dias = new ArrayList<>();
        int total = 0;
        for (int i = Resumo7Dias.DIAS - 1; i >= 0; i--) {
            LocalDate data = hoje.minusDays(i);
            boolean ok = concluidas.contains(data);
            if (ok) {
                total++;
            }
            dias.add(new DiaHabito(data, ok));
        }
        return new Resumo7Dias(dias, total);
    }
}
