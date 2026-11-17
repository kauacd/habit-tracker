<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.habittracker.service.DiaSemana, br.com.habittracker.service.ProgressoSemanal, br.com.habittracker.util.Datas" %>
<%
    request.setAttribute("titulo", "Progresso");
    ProgressoSemanal prog = (ProgressoSemanal) request.getAttribute("progresso");
    String ctx = request.getContextPath();

    // Texto equivalente ao gráfico, para leitores de tela
    StringBuilder descricao = new StringBuilder("Hábitos concluídos por dia nesta semana:");
    for (DiaSemana d : prog.getDias()) {
        descricao.append(' ').append(d.getRotulo()).append(' ');
        descricao.append(d.isFuturo() ? "ainda não chegou" : d.getConcluidas() + " de " + d.getTotalHabitos()).append('.');
    }
    int total = prog.getTotalConclusoes();
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="cabecalho-auth">
        <h1>Progresso da semana</h1>
        <p><%= Datas.abreviacao(prog.getInicio()) %> <%= Datas.diaMes(prog.getInicio()) %> a <%= Datas.abreviacao(prog.getFim()) %> <%= Datas.diaMes(prog.getFim()) %></p>
    </header>

    <% if (prog.getTotalHabitos() == 0) { %>
        <p class="vazio">Você ainda não tem hábitos para acompanhar.<br>
            <a href="<%= ctx %>/habitos/novo">Cadastre o primeiro</a>.</p>
    <% } else { %>
        <p class="resumo"><strong><%= total %></strong> <%= total == 1 ? "conclusão" : "conclusões" %> nesta semana</p>

        <div class="grafico" role="img" aria-label="<%= descricao %>">
        <% for (DiaSemana d : prog.getDias()) { %>
            <div class="coluna<%= d.isHoje() ? " hoje" : "" %><%= d.isFuturo() ? " futuro" : "" %>">
                <span class="valor"><%= d.isFuturo() ? "&ndash;" : d.getConcluidas() + "/" + d.getTotalHabitos() %></span>
                <div class="area"><div class="barra" style="height:<%= d.getPercentual() %>%"></div></div>
                <span class="rotulo"><%= d.getRotulo() %></span>
            </div>
        <% } %>
        </div>

        <% if (prog.isSemConclusoes()) { %>
            <p class="vazio">Nenhuma conclusão nesta semana ainda.<br>Conclua um hábito para o gráfico começar a crescer.</p>
        <% } %>
    <% } %>
<%@ include file="/WEB-INF/views/_nav.jspf" %>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
