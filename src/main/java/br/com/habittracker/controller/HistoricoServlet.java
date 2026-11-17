package br.com.habittracker.controller;

import br.com.habittracker.dao.ProgressoDAO;
import br.com.habittracker.service.HistoricoHabito;
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

/** US04 - Histórico individual de um hábito. */
@WebServlet("/habitos/historico")
public class HistoricoServlet extends HttpServlet {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final ProgressoService service = new ProgressoService(new ProgressoDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int habitoId;
        try {
            habitoId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HistoricoHabito historico = service.historico(Sessao.usuarioId(req), habitoId, LocalDate.now(FUSO));
        if (historico == null) { // não existe ou é de outro usuário (RN05)
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("historico", historico);
        req.getRequestDispatcher("/WEB-INF/views/historico.jsp").forward(req, resp);
    }
}
