package br.com.habittracker.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatasTest {

    private static final LocalDate QUARTA = LocalDate.of(2026, 11, 11);

    @Test
    void abreviacaoEInicialEmPortugues() {
        assertEquals("qua", Datas.abreviacao(QUARTA));
        assertEquals("Q", Datas.inicial(QUARTA));
        assertEquals("sáb", Datas.abreviacao(LocalDate.of(2026, 11, 14)));
        assertEquals("D", Datas.inicial(LocalDate.of(2026, 11, 15)));
    }

    @Test
    void diaMesTemDoisDigitos() {
        assertEquals("11/11", Datas.diaMes(QUARTA));
        assertEquals("05/03", Datas.diaMes(LocalDate.of(2026, 3, 5)));
    }

    @Test
    void semanaComecaNaSegunda() {
        assertEquals(LocalDate.of(2026, 11, 9), Datas.inicioDaSemana(QUARTA));
        assertEquals(LocalDate.of(2026, 11, 9), Datas.inicioDaSemana(LocalDate.of(2026, 11, 15)));
        assertEquals(LocalDate.of(2026, 11, 16), Datas.inicioDaSemana(LocalDate.of(2026, 11, 16)));
    }
}
