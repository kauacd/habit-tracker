<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Criar conta");
    Map<String, String> erros = (Map<String, String>) request.getAttribute("erros");
    String nome = (String) request.getAttribute("nome");
    String email = (String) request.getAttribute("email");
    if (nome == null) { nome = ""; }
    if (email == null) { email = ""; }
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="cabecalho-auth">
        <h1>Criar conta</h1>
        <p>Leva menos de um minuto.</p>
    </header>

    <form method="post" action="<%= request.getContextPath() %>/cadastro" novalidate>
        <div class="campo">
            <label for="nome">Nome</label>
            <input id="nome" name="nome" type="text" autocomplete="name" maxlength="80"
                   value="<%= Html.esc(nome) %>" <%= Html.attrsErro(erros, "nome") %>>
            <%= Html.erroCampo(erros, "nome") %>
        </div>

        <div class="campo">
            <label for="email">E-mail</label>
            <input id="email" name="email" type="email" autocomplete="email" maxlength="120"
                   value="<%= Html.esc(email) %>" <%= Html.attrsErro(erros, "email") %>>
            <%= Html.erroCampo(erros, "email") %>
        </div>

        <div class="campo">
            <label for="senha">Senha</label>
            <input id="senha" name="senha" type="password" autocomplete="new-password"
                   <%= Html.attrsErro(erros, "senha") %>>
            <p class="dica">Mínimo de 6 caracteres.</p>
            <%= Html.erroCampo(erros, "senha") %>
        </div>

        <div class="campo">
            <label for="confirmacao">Confirmar senha</label>
            <input id="confirmacao" name="confirmacao" type="password" autocomplete="new-password"
                   <%= Html.attrsErro(erros, "confirmacao") %>>
            <%= Html.erroCampo(erros, "confirmacao") %>
        </div>

        <button class="btn" type="submit">Criar conta</button>
    </form>

    <p class="troca">Já tem conta? <a href="<%= request.getContextPath() %>/login">Entrar</a></p>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
