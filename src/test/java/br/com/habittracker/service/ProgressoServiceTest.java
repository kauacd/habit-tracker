package br.com.habittracker.service;

import br.com.habittracker.dao.ProgressoRepositorio;
import br.com.habittracker.model.Habito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProgressoServiceTest {

    /** Banco "de mentira", só em memória. */
    static class RepositorioFalso implements ProgressoRepositorio {
        final Map<Integer, Habito> habitos = new HashMap<>();             // id -> hábito
        final Map<Integer, Set<LocalDate>> conclusoes = new HashMap<>();  // id -> datas

        void habito(int id, int usuarioId) {
            habitos.put(id, new Habito(id, usuarioId, "Hábito " + id, "desc"));
        }
        void concluiu(int id, LocalDate... datas) {
            conclusoes.computeIfAbsent(id, k -> new HashSet<>()).addAll(Arrays.asList(datas));
        }

        public Habito buscarHabitoDoUsuario(int habitoId, int usuarioId) {
            Habito h = habitos.get(habitoId);
            return (h != null && h.getUsuarioId() == usuarioId) ? h : null;
        }
        public Set<LocalDate> datasConcluidas(int habitoId, LocalDate de, LocalDate ate) {
            Set<LocalDate> r = new HashSet<>();
            for (LocalDate d : conclusoes.getOrDefault(habitoId, Collections.<LocalDate>emptySet())) {
                if (!d.isBefore(de) && !d.isAfter(ate)) r.add(d);
            }
            return r;
        }
        public int totalConclusoes(int habitoId) {
            return conclusoes.getOrDefault(habitoId, Collections.<LocalDate>emptySet()).size();
        }
        public Map<Integer, Set<LocalDate>> datasConcluidasPorHabito(int usuarioId, LocalDate de, LocalDate ate) {
            Map<Integer, Set<LocalDate>> r = new HashMap<>();
            for (Habito h : habitos.values()) {
                if (h.getUsuarioId() == usuarioId) r.put(h.getId(), datasConcluidas(h.getId(), de, ate));
            }
            return r;
        }
        public Map<LocalDate, Integer> concluidasPorDia(int usuarioId, LocalDate de, LocalDate ate) {
            Map<LocalDate, Integer> r = new HashMap<>();
            for (Habito h : habitos.values()) {
                if (h.getUsuarioId() != usuarioId) continue;
                for (LocalDate d : datasConcluidas(h.getId(), de, ate)) r.merge(d, 1, Integer::sum);
            }
            return r;
        }
        public int contarHabitos(int usuarioId) {
            int n = 0;
            for (Habito h : habitos.values()) if (h.getUsuarioId() == usuarioId) n++;
            return n;
        }
    }

    private static final LocalDate QUARTA = LocalDate.of(2026, 11, 11);   // quarta-feira
    private RepositorioFalso repo;
    private ProgressoService service;

    @BeforeEach
    void preparar() {
        repo = new RepositorioFalso();
        repo.habito(1, 100);
        repo.habito(2, 100);
        repo.habito(3, 200);   // de outro usuário
        service = new ProgressoService(repo);
    }

    // ---------------- US04: histórico individual ----------------

    @Test
    void historicoMostraProporcaoDeDiasConcluidos_6De7() {
        for (int i = 0; i < 6; i++) repo.concluiu(1, QUARTA.minusDays(i));   // só falta o dia mais antigo
        HistoricoHabito h = service.historico(100, 1, QUARTA);
        assertEquals(6, h.getResumo().getConcluidos());
        assertEquals(85, h.getResumo().getPercentual());   // 6/7 = 85%
        assertFalse(h.isSemDados());
    }

    @Test
    void historicoDeUmDiaDe7Da14Porcento() {
        repo.concluiu(1, QUARTA);
        assertEquals(14, service.historico(100, 1, QUARTA).getResumo().getPercentual());
    }

    @Test
    void historicoSempreTemSeteDiasDoMaisAntigoAteHoje() {
        repo.concluiu(1, QUARTA);
        List<DiaHabito> dias = service.historico(100, 1, QUARTA).getResumo().getDias();
        assertEquals(7, dias.size());
        assertEquals(QUARTA.minusDays(6), dias.get(0).getData());
        assertEquals(QUARTA, dias.get(6).getData());
        assertTrue(dias.get(6).isConcluido());
    }

    @Test
    void historicoSemNenhumaConclusaoAvisaQueNaoHaDados() {
        HistoricoHabito h = service.historico(100, 1, QUARTA);
        assertTrue(h.isSemDados());
        assertEquals(0, h.getTotalConclusoes());
    }

    @Test
    void conclusaoAntigaNaoContaNos7DiasMasJaHaDados() {
        repo.concluiu(1, QUARTA.minusDays(30));
        HistoricoHabito h = service.historico(100, 1, QUARTA);
        assertFalse(h.isSemDados());
        assertEquals(0, h.getResumo().getConcluidos());
        assertEquals(1, h.getTotalConclusoes());
    }

    @Test
    void rn05_historicoDeHabitoDeOutroUsuarioNaoEhMostrado() {
        assertNull(service.historico(100, 3, QUARTA));
        assertNull(service.historico(100, 999, QUARTA));
    }

    @Test
    void resumosDosCartoesSoIncluemHabitosDoUsuario() {
        repo.concluiu(1, QUARTA, QUARTA.minusDays(1));
        Map<Integer, Resumo7Dias> r = service.resumosPorHabito(100, Arrays.asList(1, 2), QUARTA);
        assertEquals(2, r.get(1).getConcluidos());
        assertEquals(0, r.get(2).getConcluidos());
    }

    // ---------------- US05: progresso semanal ----------------

    @Test
    void semanaComecaNaSegundaEDiasFuturosFicamVazios() {
        ProgressoSemanal p = service.progressoSemanal(100, QUARTA);
        assertEquals(LocalDate.of(2026, 11, 9), p.getInicio());    // segunda
        assertEquals(LocalDate.of(2026, 11, 15), p.getFim());      // domingo
        assertEquals(7, p.getDias().size());
        assertFalse(p.getDias().get(2).isFuturo());   // quarta (hoje)
        assertTrue(p.getDias().get(2).isHoje());
        assertTrue(p.getDias().get(3).isFuturo());    // quinta
    }

    @Test
    void graficoContaHabitosConcluidosPorDia() {
        repo.concluiu(1, LocalDate.of(2026, 11, 9), QUARTA);
        repo.concluiu(2, LocalDate.of(2026, 11, 9));
        ProgressoSemanal p = service.progressoSemanal(100, QUARTA);
        assertEquals(2, p.getDias().get(0).getConcluidas());      // segunda: 2 de 2
        assertEquals(100, p.getDias().get(0).getPercentual());
        assertEquals(0, p.getDias().get(1).getConcluidas());      // terça
        assertEquals(1, p.getDias().get(2).getConcluidas());      // quarta: 1 de 2
        assertEquals(50, p.getDias().get(2).getPercentual());
        assertEquals(3, p.getTotalConclusoes());
        assertFalse(p.isSemConclusoes());
    }

    @Test
    void semanaVaziaNaoGeraErroEFicaZerada() {
        ProgressoSemanal p = service.progressoSemanal(100, QUARTA);
        assertTrue(p.isSemConclusoes());
        for (DiaSemana d : p.getDias()) {
            assertEquals(0, d.getPercentual());
        }
    }

    @Test
    void usuarioSemHabitosNaoCausaDivisaoPorZero() {
        ProgressoSemanal p = service.progressoSemanal(999, QUARTA);
        assertEquals(0, p.getTotalHabitos());
        assertEquals(0, p.getDias().get(0).getPercentual());
    }

    @Test
    void conclusoesDeOutroUsuarioNaoEntramNoGrafico() {
        repo.concluiu(3, QUARTA);
        assertTrue(service.progressoSemanal(100, QUARTA).isSemConclusoes());
    }

    @Test
    void naSegundaASemanaTemComecoHoje() {
        ProgressoSemanal p = service.progressoSemanal(100, LocalDate.of(2026, 11, 16));
        assertEquals(LocalDate.of(2026, 11, 16), p.getInicio());
        assertTrue(p.getDias().get(0).isHoje());
    }

    @Test
    void noDomingoASemanaTerminaHoje() {
        ProgressoSemanal p = service.progressoSemanal(100, LocalDate.of(2026, 11, 15));
        assertEquals(LocalDate.of(2026, 11, 9), p.getInicio());
        assertTrue(p.getDias().get(6).isHoje());
        assertFalse(p.getDias().get(6).isFuturo());
    }
}
