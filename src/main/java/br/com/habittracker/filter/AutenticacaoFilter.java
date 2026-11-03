package br.com.habittracker.filter;

import br.com.habittracker.util.Sessao;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Bloqueia as telas internas para quem não fez login (US07). */
@WebFilter(urlPatterns = {"/habitos", "/habitos/*", "/logout"})
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (Sessao.usuarioId(req) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Evita que o botão "voltar" mostre telas internas depois do logout
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}
