package br.com.habittracker.controller;

import br.com.habittracker.dao.HabitoDAO;
import br.com.habittracker.dao.ProgressoDAO;
import br.com.habittracker.model.Habito;
import br.com.habittracker.service.ProgressoService;
import br.com.habittracker.util.Sessao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** US02, US03 e US04 - Tela "Meus Hábitos": só os hábitos do usuário, com o status de hoje e os últimos 7 dias. */
@WebServlet("/habitos")
public class HabitosServlet extends HttpServlet {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final HabitoDAO habitoDAO = new HabitoDAO();
    private final ProgressoService progressoService = new ProgressoService(new ProgressoDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int usuarioId = Sessao.usuarioId(req); // o filtro garante que está logado
        LocalDateTime agora = LocalDateTime.now(FUSO);

        LocalDate hoje = agora.toLocalDate();
        List<Habito> habitos = habitoDAO.listarPorUsuario(usuarioId, hoje);
        List<Integer> ids = new ArrayList<>();
        for (Habito h : habitos) {
            ids.add(h.getId());
        }
        req.setAttribute("habitos", habitos);
        req.setAttribute("resumos", progressoService.resumosPorHabito(usuarioId, ids, hoje));
        req.setAttribute("aba", "habitos");
        req.setAttribute("saudacao", saudacao(agora.getHour()));
        req.setAttribute("dataHoje", agora.format(
                DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR"))));
        req.setAttribute("nomeUsuario", Sessao.usuarioNome(req));
        req.setAttribute("criado", "1".equals(req.getParameter("criado")));
        req.setAttribute("resultado", req.getParameter("resultado"));
        req.getRequestDispatcher("/WEB-INF/views/habitos.jsp").forward(req, resp);
    }

    private String saudacao(int hora) {
        if (hora < 12) {
            return "Bom dia";
        }
        return hora < 18 ? "Boa tarde" : "Boa noite";
    }
}
