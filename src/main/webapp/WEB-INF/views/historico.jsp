<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, br.com.habittracker.service.DiaHabito, br.com.habittracker.service.HistoricoHabito, br.com.habittracker.service.Resumo7Dias, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Histórico");
    HistoricoHabito hist = (HistoricoHabito) request.getAttribute("historico");
    Resumo7Dias resumo = hist.getResumo();
    List<DiaHabito> dias = resumo.getDias();
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <a class="voltar" href="<%= ctx %>/habitos">&larr; Hábitos</a>

    <header class="cabecalho-auth">
        <h1><%= Html.esc(hist.getNome()) %></h1>
        <p><%= Html.esc(hist.getDescricao()) %></p>
    </header>

    <% if (hist.isSemDados()) { %>
        <p class="vazio">Ainda não há dados suficientes.<br>Conclua este hábito para começar o seu histórico.</p>
    <% } else { %>
        <p class="destaque"><%= resumo.getConcluidos() %>/7 dias &mdash; <%= resumo.getPercentual() %>%</p>
        <p class="legenda">dias concluídos nos últimos 7 dias</p>

        <ul class="hist">
        <% for (int i = dias.size() - 1; i >= 0; i--) { DiaHabito d = dias.get(i); %>
            <li>
                <span><%= d.getTitulo() %><%= i == dias.size() - 1 ? " (hoje)" : "" %></span>
                <% if (d.isConcluido()) { %>
                    <span class="estado ok-txt">&#10003; Concluído</span>
                <% } else { %>
                    <span class="estado nao-txt">Não concluído</span>
                <% } %>
            </li>
        <% } %>
        </ul>

        <p class="legenda">Total de conclusões deste hábito: <strong><%= hist.getTotalConclusoes() %></strong></p>
    <% } %>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
