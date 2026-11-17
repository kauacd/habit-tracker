package br.com.habittracker.controller;

import br.com.habittracker.dao.ProgressoDAO;
import br.com.habittracker.service.ProgressoService;
import br.com.habittracker.util.Sessao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/** US05 - Tela de Progresso com o gráfico da semana. */
@WebServlet("/progresso")
public class ProgressoServlet extends HttpServlet {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final ProgressoService service = new ProgressoService(new ProgressoDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("progresso", service.progressoSemanal(Sessao.usuarioId(req), LocalDate.now(FUSO)));
        req.setAttribute("aba", "progresso");
        req.getRequestDispatcher("/WEB-INF/views/progresso.jsp").forward(req, resp);
    }
}
