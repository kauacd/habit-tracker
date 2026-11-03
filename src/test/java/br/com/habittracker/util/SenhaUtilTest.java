package br.com.habittracker.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenhaUtilTest {

    @Test
    void hashNaoGuardaASenhaEmTextoPuro() {
        String hash = SenhaUtil.gerarHash("minhaSenha123");
        assertFalse(hash.contains("minhaSenha123"));
    }

    @Test
    void senhaCorretaE_verificadaComSucesso() {
        String hash = SenhaUtil.gerarHash("minhaSenha123");
        assertTrue(SenhaUtil.verificar("minhaSenha123", hash));
    }

    @Test
    void senhaIncorretaEhRejeitada() {
        String hash = SenhaUtil.gerarHash("minhaSenha123");
        assertFalse(SenhaUtil.verificar("outraSenha", hash));
    }

    @Test
    void mesmaSenhaGeraHashesDiferentesPorCausaDoSalt() {
        assertNotEquals(SenhaUtil.gerarHash("abc123"), SenhaUtil.gerarHash("abc123"));
    }

    @Test
    void hashEmFormatoInvalidoNaoQuebra() {
        assertFalse(SenhaUtil.verificar("abc123", "lixo"));
        assertFalse(SenhaUtil.verificar("abc123", null));
    }
}
