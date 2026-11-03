package br.com.habittracker.controller;

import br.com.habittracker.dao.HabitoDAO;
import br.com.habittracker.model.Habito;
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

/** US01 - Cadastrar hábito. */
@WebServlet("/habitos/novo")
public class NovoHabitoServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/habito-novo.jsp";
    private final HabitoDAO habitoDAO = new HabitoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("erros", new LinkedHashMap<String, String>());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String descricao = req.getParameter("descricao");

        Map<String, String> erros = Validador.validarHabito(nome, descricao);

        if (erros.isEmpty()) {
            Habito habito = new Habito();
            habito.setUsuarioId(Sessao.usuarioId(req)); // sempre o usuário da sessão, nunca um id vindo do formulário
            habito.setNome(nome.trim());
            habito.setDescricao(descricao.trim());
            habitoDAO.inserir(habito);
            resp.sendRedirect(req.getContextPath() + "/habitos?criado=1");
            return;
        }

        req.setAttribute("erros", erros);
        req.setAttribute("nome", nome);
        req.setAttribute("descricao", descricao);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
