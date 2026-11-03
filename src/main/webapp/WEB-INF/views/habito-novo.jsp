<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map, br.com.habittracker.util.Html" %>
<%
    request.setAttribute("titulo", "Novo hábito");
    Map<String, String> erros = (Map<String, String>) request.getAttribute("erros");
    String nome = (String) request.getAttribute("nome");
    String descricao = (String) request.getAttribute("descricao");
    if (nome == null) { nome = ""; }
    if (descricao == null) { descricao = ""; }
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="cabecalho-auth">
        <h1>Novo hábito</h1>
    </header>

    <form method="post" action="<%= request.getContextPath() %>/habitos/novo" novalidate>
        <div class="campo">
            <label for="nome">Nome</label>
            <input id="nome" name="nome" type="text" maxlength="80" placeholder="Ex.: Beber água"
                   value="<%= Html.esc(nome) %>" <%= Html.attrsErro(erros, "nome") %>>
            <%= Html.erroCampo(erros, "nome") %>
        </div>

        <div class="campo">
            <label for="descricao">Descrição</label>
            <input id="descricao" name="descricao" type="text" maxlength="255" placeholder="Ex.: 2 litros por dia"
                   value="<%= Html.esc(descricao) %>" <%= Html.attrsErro(erros, "descricao") %>>
            <%= Html.erroCampo(erros, "descricao") %>
        </div>

        <button class="btn" type="submit">Salvar</button>
        <a class="btn-link" href="<%= request.getContextPath() %>/habitos">Cancelar</a>
    </form>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
