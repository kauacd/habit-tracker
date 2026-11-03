package br.com.habittracker.util;

import br.com.habittracker.model.Usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** Guarda e consulta quem está logado. */
public class Sessao {

    private static final String ATR_ID = "usuarioId";
    private static final String ATR_NOME = "usuarioNome";

    private Sessao() { }

    /** Inicia uma sessão nova para o usuário (descarta a anterior). */
    public static void iniciar(HttpServletRequest req, Usuario usuario) {
        HttpSession antiga = req.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        HttpSession sessao = req.getSession(true);
        sessao.setAttribute(ATR_ID, usuario.getId());
        sessao.setAttribute(ATR_NOME, usuario.getNome());
    }

    public static void encerrar(HttpServletRequest req) {
        HttpSession sessao = req.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
    }

    /** Id do usuário logado ou null se ninguém estiver logado. */
    public static Integer usuarioId(HttpServletRequest req) {
        HttpSession sessao = req.getSession(false);
        return sessao == null ? null : (Integer) sessao.getAttribute(ATR_ID);
    }

    public static String usuarioNome(HttpServletRequest req) {
        HttpSession sessao = req.getSession(false);
        return sessao == null ? null : (String) sessao.getAttribute(ATR_NOME);
    }
}
