<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Entrar");
    Map<String, String> erros = (Map<String, String>) request.getAttribute("erros");
    String email = (String) request.getAttribute("email");
    if (email == null) { email = ""; }
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="cabecalho-auth">
        <h1>Habit Tracker</h1>
        <p>Entre para acompanhar seus hábitos.</p>
    </header>

    <% if (erros.containsKey("geral")) { %>
        <p class="alerta" role="alert"><%= Html.esc(erros.get("geral")) %></p>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/login" novalidate>
        <div class="campo">
            <label for="email">E-mail</label>
            <input id="email" name="email" type="email" autocomplete="email"
                   value="<%= Html.esc(email) %>" <%= Html.attrsErro(erros, "email") %>>
            <%= Html.erroCampo(erros, "email") %>
        </div>

        <div class="campo">
            <label for="senha">Senha</label>
            <input id="senha" name="senha" type="password" autocomplete="current-password"
                   <%= Html.attrsErro(erros, "senha") %>>
            <%= Html.erroCampo(erros, "senha") %>
        </div>

        <button class="btn" type="submit">Entrar</button>
    </form>

    <p class="troca">Ainda não tem conta? <a href="<%= request.getContextPath() %>/cadastro">Cadastre-se</a></p>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
