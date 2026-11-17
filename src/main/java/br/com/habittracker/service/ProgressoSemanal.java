package br.com.habittracker.service;

import java.time.LocalDate;
import java.util.List;

/** Dados da tela de Progresso: a semana atual, de segunda a domingo (US05). */
public class ProgressoSemanal {
    private final LocalDate inicio;
    private final LocalDate fim;
    private final List<DiaSemana> dias;
    private final int totalHabitos;
    private final int totalConclusoes;

    public ProgressoSemanal(LocalDate inicio, LocalDate fim, List<DiaSemana> dias, int totalHabitos, int totalConclusoes) {
        this.inicio = inicio;
        this.fim = fim;
        this.dias = dias;
        this.totalHabitos = totalHabitos;
        this.totalConclusoes = totalConclusoes;
    }

    public LocalDate getInicio() { return inicio; }
    public LocalDate getFim() { return fim; }
    public List<DiaSemana> getDias() { return dias; }
    public int getTotalHabitos() { return totalHabitos; }
    public int getTotalConclusoes() { return totalConclusoes; }
    public boolean isSemConclusoes() { return totalConclusoes == 0; }
}
