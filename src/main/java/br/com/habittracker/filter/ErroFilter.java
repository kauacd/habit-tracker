package br.com.habittracker.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Estabilização: se algo inesperado acontecer (ex.: banco fora do ar), o usuário
 * vê uma tela amigável em vez da pilha de erros do Tomcat. O erro completo vai para o log.
 */
@WebFilter("/*")
public class ErroFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (RuntimeException | ServletException e) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            req.getServletContext().log("Erro inesperado em " + req.getRequestURI(), e);
            if (resp.isCommitted()) {
                throw e;
            }
            resp.reset();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
