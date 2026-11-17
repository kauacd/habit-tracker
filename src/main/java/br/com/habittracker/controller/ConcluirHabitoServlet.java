package br.com.habittracker.controller;

import br.com.habittracker.dao.ConclusaoDAO;
import br.com.habittracker.service.ConclusaoService;
import br.com.habittracker.service.ConclusaoService.Resultado;
import br.com.habittracker.util.Sessao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/** US03 - Marcar hábito como concluído hoje. */
@WebServlet("/habitos/concluir")
public class ConcluirHabitoServlet extends HttpServlet {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final ConclusaoService service = new ConclusaoService(new ConclusaoDAO());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int habitoId;
        try {
            habitoId = Integer.parseInt(req.getParameter("habitoId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int usuarioId = Sessao.usuarioId(req); // o filtro garante que está logado
        Resultado resultado = service.concluir(usuarioId, habitoId, LocalDate.now(FUSO));

        // Chamada feita pelo JavaScript da tela: responde só com o status
        boolean viaJs = "fetch".equals(req.getHeader("X-Requested-With"));
        if (viaJs) {
            resp.setStatus(resultado == Resultado.NAO_ENCONTRADO
                    ? HttpServletResponse.SC_NOT_FOUND : HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        // Sem JavaScript: volta para a lista com um aviso
        if (resultado == Resultado.NAO_ENCONTRADO) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String aviso = resultado == Resultado.CONCLUIDO ? "concluido" : "ja";
        resp.sendRedirect(req.getContextPath() + "/habitos?resultado=" + aviso);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/habitos");
    }
}
