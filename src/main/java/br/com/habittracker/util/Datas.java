package br.com.habittracker.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/** Auxiliares de data em português (não dependem do idioma do computador). */
public class Datas {

    private static final String[] ABREV = {"seg", "ter", "qua", "qui", "sex", "sáb", "dom"};
    private static final String[] INICIAL = {"S", "T", "Q", "Q", "S", "S", "D"};

    private Datas() { }

    /** "seg", "ter", "qua"... */
    public static String abreviacao(LocalDate d) {
        return ABREV[d.getDayOfWeek().getValue() - 1];
    }

    /** "S", "T", "Q"... */
    public static String inicial(LocalDate d) {
        return INICIAL[d.getDayOfWeek().getValue() - 1];
    }

    /** "11/11" */
    public static String diaMes(LocalDate d) {
        return String.format("%02d/%02d", d.getDayOfMonth(), d.getMonthValue());
    }

    /** A semana começa na segunda-feira. */
    public static LocalDate inicioDaSemana(LocalDate d) {
        return d.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}
