package br.com.habittracker.service;

import br.com.habittracker.util.Datas;

import java.time.LocalDate;

/** Uma coluna do gráfico semanal (US05). */
public class DiaSemana {
    private final LocalDate data;
    private final int concluidas;
    private final int totalHabitos;
    private final boolean futuro;
    private final boolean hoje;

    public DiaSemana(LocalDate data, int concluidas, int totalHabitos, boolean futuro, boolean hoje) {
        this.data = data;
        this.concluidas = concluidas;
        this.totalHabitos = totalHabitos;
        this.futuro = futuro;
        this.hoje = hoje;
    }

    public LocalDate getData() { return data; }
    public int getConcluidas() { return concluidas; }
    public int getTotalHabitos() { return totalHabitos; }
    public boolean isFuturo() { return futuro; }
    public boolean isHoje() { return hoje; }

    /** "Seg", "Ter"... */
    public String getRotulo() {
        String a = Datas.abreviacao(data);
        return Character.toUpperCase(a.charAt(0)) + a.substring(1);
    }

    /** Altura da barra (0 a 100). Sem hábitos não há divisão por zero. */
    public int getPercentual() {
        return totalHabitos == 0 ? 0 : concluidas * 100 / totalHabitos;
    }
}
