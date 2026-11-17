package br.com.habittracker.service;

import java.util.List;

/** Últimos 7 dias de um hábito (do mais antigo até hoje). Ex.: 6/7 dias - 85%. */
public class Resumo7Dias {
    public static final int DIAS = 7;

    private final List<DiaHabito> dias;
    private final int concluidos;

    public Resumo7Dias(List<DiaHabito> dias, int concluidos) {
        this.dias = dias;
        this.concluidos = concluidos;
    }

    public List<DiaHabito> getDias() { return dias; }
    public int getConcluidos() { return concluidos; }

    /** Proporção de dias concluídos, sem arredondar para cima (6/7 = 85%). */
    public int getPercentual() { return concluidos * 100 / DIAS; }
}
