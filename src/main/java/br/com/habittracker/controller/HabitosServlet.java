package br.com.habittracker.controller;

import br.com.habittracker.dao.HabitoDAO;
import br.com.habittracker.util.Sessao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** US02 e US03 - Tela "Meus Hábitos" (só os hábitos do usuário logado, com o status de hoje). */
@WebServlet("/habitos")
public class HabitosServlet extends HttpServlet {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final HabitoDAO habitoDAO = new HabitoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int usuarioId = Sessao.usuarioId(req); // o filtro garante que está logado
        LocalDateTime agora = LocalDateTime.now(FUSO);

        req.setAttribute("habitos", habitoDAO.listarPorUsuario(usuarioId, agora.toLocalDate()));
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
