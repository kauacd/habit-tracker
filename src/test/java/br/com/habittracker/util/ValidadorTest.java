package br.com.habittracker.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorTest {

    // ---- US06: cadastro de usuário ----
    @Test
    void cadastroValidoNaoTemErros() {
        assertTrue(Validador.validarCadastro("Ana", "ana@email.com", "123456", "123456").isEmpty());
    }

    @Test
    void cadastroSemNomeEhRecusado() {
        Map<String, String> erros = Validador.validarCadastro("  ", "ana@email.com", "123456", "123456");
        assertTrue(erros.containsKey("nome"));
    }

    @Test
    void cadastroComEmailInvalidoEhRecusado() {
        assertTrue(Validador.validarCadastro("Ana", "ana@", "123456", "123456").containsKey("email"));
        assertTrue(Validador.validarCadastro("Ana", "", "123456", "123456").containsKey("email"));
    }

    @Test
    void cadastroComSenhaCurtaEhRecusado() {
        assertTrue(Validador.validarCadastro("Ana", "ana@email.com", "123", "123").containsKey("senha"));
    }

    @Test
    void cadastroComConfirmacaoDiferenteEhRecusado() {
        assertTrue(Validador.validarCadastro("Ana", "ana@email.com", "123456", "654321").containsKey("confirmacao"));
    }

    @Test
    void emailEhNormalizado() {
        assertEquals("ana@email.com", Validador.normalizarEmail("  ANA@Email.com "));
        assertEquals("", Validador.normalizarEmail(null));
    }

    // ---- US07: login ----
    @Test
    void loginComCamposVaziosEhRecusado() {
        Map<String, String> erros = Validador.validarLogin("", "");
        assertTrue(erros.containsKey("email"));
        assertTrue(erros.containsKey("senha"));
    }

    @Test
    void loginComCamposPreenchidosPassaNaValidacao() {
        assertTrue(Validador.validarLogin("ana@email.com", "123456").isEmpty());
    }

    // ---- US01: cadastrar hábito ----
    @Test
    void habitoValidoNaoTemErros() {
        assertTrue(Validador.validarHabito("Beber água", "2 litros por dia").isEmpty());
    }

    @Test
    void habitoSemNomeEhRecusado() {
        assertTrue(Validador.validarHabito("", "2 litros").containsKey("nome"));
    }

    @Test
    void habitoSemDescricaoEhRecusado() {
        assertTrue(Validador.validarHabito("Ler", " ").containsKey("descricao"));
    }
}
