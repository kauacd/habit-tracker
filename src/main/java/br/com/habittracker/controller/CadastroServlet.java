package br.com.habittracker.controller;

import br.com.habittracker.dao.EmailJaCadastradoException;
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

/** US06 - Cadastro de usuário. */
@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/cadastro.jsp";
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
        String nome = req.getParameter("nome");
        String email = Validador.normalizarEmail(req.getParameter("email"));
        String senha = req.getParameter("senha");
        String confirmacao = req.getParameter("confirmacao");

        Map<String, String> erros = Validador.validarCadastro(nome, email, senha, confirmacao);

        if (erros.isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNome(nome.trim());
            usuario.setEmail(email);
            usuario.setSenhaHash(SenhaUtil.gerarHash(senha));
            try {
                usuarioDAO.inserir(usuario);
                Sessao.iniciar(req, usuario);
                resp.sendRedirect(req.getContextPath() + "/habitos");
                return;
            } catch (EmailJaCadastradoException e) {
                erros.put("email", "Este e-mail já está cadastrado.");
            }
        }

        // Volta para o formulário mantendo o que foi digitado (menos as senhas)
        req.setAttribute("erros", erros);
        req.setAttribute("nome", nome);
        req.setAttribute("email", email);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
