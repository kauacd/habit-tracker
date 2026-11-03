package br.com.habittracker.controller;

import br.com.habittracker.dao.UsuarioDAO;
import br.com.habittracker.model.Usuario;
import br.com.habittracker.util.SenhaUtil;
import br.com.habittracker.util.Sessao;
import br.com.habittracker.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/** US07 - Login. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/login.jsp";
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Sessao.usuarioId(req) != null) {
            resp.sendRedirect(req.getContextPath() + "/habitos");
            return;
        }
        req.setAttribute("erros", new LinkedHashMap<String, String>());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = Validador.normalizarEmail(req.getParameter("email"));
        String senha = req.getParameter("senha");

        Map<String, String> erros = Validador.validarLogin(email, senha);

        if (erros.isEmpty()) {
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            if (usuario != null && SenhaUtil.verificar(senha, usuario.getSenhaHash())) {
                Sessao.iniciar(req, usuario);
                resp.sendRedirect(req.getContextPath() + "/habitos");
                return;
            }
            // Mensagem genérica: não revela se foi o e-mail ou a senha que errou
            erros.put("geral", "E-mail ou senha incorretos.");
        }

        req.setAttribute("erros", erros);
        req.setAttribute("email", email);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
