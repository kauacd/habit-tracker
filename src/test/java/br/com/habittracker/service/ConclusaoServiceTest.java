package br.com.habittracker.service;

import br.com.habittracker.dao.ConclusaoJaExisteException;
import br.com.habittracker.dao.ConclusaoRepositorio;
import br.com.habittracker.service.ConclusaoService.Resultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConclusaoServiceTest {

    /** Banco "de mentira", só em memória. */
    static class RepositorioFalso implements ConclusaoRepositorio {
        final Map<Integer, Integer> donos = new HashMap<>();   // habitoId -> usuarioId
        final List<String> conclusoes = new ArrayList<>();     // "habitoId|data"
        boolean simularCorrida = false;                        // banco recusa por UNIQUE na hora de gravar

        public boolean habitoPertenceAoUsuario(int habitoId, int usuarioId) {
            return Integer.valueOf(usuarioId).equals(donos.get(habitoId));
        }
        public boolean existeConclusao(int habitoId, LocalDate data) {
            return !simularCorrida && conclusoes.contains(habitoId + "|" + data);
        }
        public void inserir(int habitoId, LocalDate data) throws ConclusaoJaExisteException {
            if (simularCorrida || conclusoes.contains(habitoId + "|" + data)) {
                throw new ConclusaoJaExisteException();
            }
            conclusoes.add(habitoId + "|" + data);
        }
    }

    private static final LocalDate HOJE = LocalDate.of(2026, 11, 10);
    private RepositorioFalso repo;
    private ConclusaoService service;

    @BeforeEach
    void preparar() {
        repo = new RepositorioFalso();
        repo.donos.put(1, 100);   // hábito 1 é do usuário 100
        repo.donos.put(2, 200);   // hábito 2 é do usuário 200
        service = new ConclusaoService(repo);
    }

    @Test
    void concluirHabitoDoProprioUsuarioRegistraAConclusao() {
        assertEquals(Resultado.CONCLUIDO, service.concluir(100, 1, HOJE));
        assertEquals(1, repo.conclusoes.size());
    }

    @Test
    void rn02_conclusaoFicaAssociadaADataDeHoje() {
        service.concluir(100, 1, HOJE);
        assertEquals("1|2026-11-10", repo.conclusoes.get(0));
    }

    @Test
    void rn01_concluirDuasVezesNoMesmoDiaNaoCriaNovoRegistro() {
        service.concluir(100, 1, HOJE);
        assertEquals(Resultado.JA_CONCLUIDO, service.concluir(100, 1, HOJE));
        assertEquals(1, repo.conclusoes.size());
    }

    @Test
    void rn01_noDiaSeguintePodeConcluirDeNovo() {
        service.concluir(100, 1, HOJE);
        assertEquals(Resultado.CONCLUIDO, service.concluir(100, 1, HOJE.plusDays(1)));
        assertEquals(2, repo.conclusoes.size());
    }

    @Test
    void rn05_naoConcluiHabitoDeOutroUsuario() {
        assertEquals(Resultado.NAO_ENCONTRADO, service.concluir(100, 2, HOJE));
        assertTrue(repo.conclusoes.isEmpty());
    }

    @Test
    void habitoInexistenteNaoEhEncontrado() {
        assertEquals(Resultado.NAO_ENCONTRADO, service.concluir(100, 999, HOJE));
        assertTrue(repo.conclusoes.isEmpty());
    }

    @Test
    void duasRequisicoesAoMesmoTempoNaoGeramDuplicidade() {
        repo.simularCorrida = true;   // a checagem passa, mas o banco recusa na gravação
        assertEquals(Resultado.JA_CONCLUIDO, service.concluir(100, 1, HOJE));
        assertTrue(repo.conclusoes.isEmpty());
    }
}
