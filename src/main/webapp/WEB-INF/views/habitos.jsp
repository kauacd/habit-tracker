<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, br.com.habittracker.model.Habito, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Meus Hábitos");
    List<Habito> habitos = (List<Habito>) request.getAttribute("habitos");
    boolean criado = Boolean.TRUE.equals(request.getAttribute("criado"));
    String resultado = (String) request.getAttribute("resultado");
    String ctx = request.getContextPath();
    int feitos = 0;
    for (Habito h : habitos) {
        if (h.isConcluidoHoje()) { feitos++; }
    }
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="topo">
        <div>
            <p class="data"><%= Html.esc((String) request.getAttribute("dataHoje")) %></p>
            <h1><%= Html.esc((String) request.getAttribute("saudacao")) %>, <%= Html.esc((String) request.getAttribute("nomeUsuario")) %></h1>
        </div>
        <form method="post" action="<%= ctx %>/logout">
            <button class="btn-sec" type="submit">Sair</button>
        </form>
    </header>

    <% if (criado) { %>
        <p class="ok" role="status">Hábito cadastrado com sucesso.</p>
    <% } %>
    <% if ("concluido".equals(resultado)) { %>
        <p class="ok" role="status">Hábito concluído. Muito bem!</p>
    <% } else if ("ja".equals(resultado)) { %>
        <p class="alerta" role="status">Este hábito já foi concluído hoje.</p>
    <% } %>

    <% if (habitos.isEmpty()) { %>
        <p class="vazio">Você ainda não cadastrou nenhum hábito.<br>Toque no <strong>+</strong> para começar.</p>
    <% } else { %>
        <p class="resumo"><span id="qtd-feitos"><%= feitos %></span> de <%= habitos.size() %> concluídos hoje</p>
        <ul class="lista">
        <% for (Habito h : habitos) { %>
            <li class="card<%= h.isConcluidoHoje() ? " feito" : "" %>">
                <div class="card-info">
                    <h2><%= Html.esc(h.getNome()) %></h2>
                    <p><%= Html.esc(h.getDescricao()) %></p>
                </div>
                <% if (h.isConcluidoHoje()) { %>
                    <span class="check feito" role="img" aria-label="Concluído hoje">&#10003;</span>
                <% } else { %>
                    <form class="form-concluir" method="post" action="<%= ctx %>/habitos/concluir">
                        <input type="hidden" name="habitoId" value="<%= h.getId() %>">
                        <button class="check" type="submit" title="Concluir hoje"
                                aria-label="Marcar <%= Html.esc(h.getNome()) %> como concluído hoje"></button>
                    </form>
                <% } %>
            </li>
        <% } %>
        </ul>
    <% } %>

    <a class="fab" href="<%= ctx %>/habitos/novo" aria-label="Adicionar hábito" title="Adicionar hábito">+</a>
    <script src="<%= ctx %>/js/app.js" defer></script>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
