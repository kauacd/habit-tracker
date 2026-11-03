package br.com.habittracker.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Validações de formulário. Cada método devolve um mapa campo -> mensagem.
 * Mapa vazio = tudo certo.
 */
public class Validador {

    public static final int SENHA_MINIMA = 6;
    private static final String REGEX_EMAIL = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    private Validador() { }

    public static String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    public static Map<String, String> validarCadastro(String nome, String email, String senha, String confirmacao) {
        Map<String, String> erros = new LinkedHashMap<>();

        if (vazio(nome)) {
            erros.put("nome", "Informe seu nome.");
        } else if (nome.trim().length() > 80) {
            erros.put("nome", "O nome deve ter no máximo 80 caracteres.");
        }

        if (vazio(email)) {
            erros.put("email", "Informe seu e-mail.");
        } else if (email.trim().length() > 120 || !email.trim().matches(REGEX_EMAIL)) {
            erros.put("email", "Informe um e-mail válido.");
        }

        if (senha == null || senha.isEmpty()) {
            erros.put("senha", "Crie uma senha.");
        } else if (senha.length() < SENHA_MINIMA) {
            erros.put("senha", "A senha deve ter pelo menos " + SENHA_MINIMA + " caracteres.");
        } else if (!senha.equals(confirmacao)) {
            erros.put("confirmacao", "As senhas não conferem.");
        }

        return erros;
    }

    public static Map<String, String> validarLogin(String email, String senha) {
        Map<String, String> erros = new LinkedHashMap<>();
        if (vazio(email)) {
            erros.put("email", "Informe seu e-mail.");
        }
        if (senha == null || senha.isEmpty()) {
            erros.put("senha", "Informe sua senha.");
        }
        return erros;
    }

    public static Map<String, String> validarHabito(String nome, String descricao) {
        Map<String, String> erros = new LinkedHashMap<>();
        if (vazio(nome)) {
            erros.put("nome", "Informe o nome do hábito.");
        } else if (nome.trim().length() > 80) {
            erros.put("nome", "O nome deve ter no máximo 80 caracteres.");
        }
        if (vazio(descricao)) {
            erros.put("descricao", "Informe uma descrição.");
        } else if (descricao.trim().length() > 255) {
            erros.put("descricao", "A descrição deve ter no máximo 255 caracteres.");
        }
        return erros;
    }

    private static boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
