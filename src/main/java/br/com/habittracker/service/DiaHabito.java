package br.com.habittracker.service;

import br.com.habittracker.util.Datas;

import java.time.LocalDate;

/** Um dia da janela de 7 dias de um hábito: foi concluído ou não. */
public class DiaHabito {
    private final LocalDate data;
    private final boolean concluido;

    public DiaHabito(LocalDate data, boolean concluido) {
        this.data = data;
        this.concluido = concluido;
    }

    public LocalDate getData() { return data; }
    public boolean isConcluido() { return concluido; }
    public String getInicial() { return Datas.inicial(data); }
    public String getTitulo() { return Datas.abreviacao(data) + " " + Datas.diaMes(data); }
}
