<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, br.com.habittracker.model.Habito, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Meus Hábitos");
    List<Habito> habitos = (List<Habito>) request.getAttribute("habitos");
    boolean criado = Boolean.TRUE.equals(request.getAttribute("criado"));
    String ctx = request.getContextPath();
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

    <% if (habitos.isEmpty()) { %>
        <p class="vazio">Você ainda não cadastrou nenhum hábito.<br>Toque no <strong>+</strong> para começar.</p>
    <% } else { %>
        <ul class="lista">
        <% for (Habito h : habitos) { %>
            <li class="card">
                <h2><%= Html.esc(h.getNome()) %></h2>
                <p><%= Html.esc(h.getDescricao()) %></p>
            </li>
        <% } %>
        </ul>
    <% } %>

    <a class="fab" href="<%= ctx %>/habitos/novo" aria-label="Adicionar hábito" title="Adicionar hábito">+</a>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
