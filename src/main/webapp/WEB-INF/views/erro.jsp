<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("titulo", "Algo deu errado");
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/_topo.jspf" %>
    <header class="cabecalho-auth">
        <h1>Algo deu errado</h1>
        <p>Não foi possível concluir a ação. Tente novamente em instantes.</p>
    </header>
    <a class="btn btn-bloco" href="<%= ctx %>/habitos">Voltar para Meus Hábitos</a>
<%@ include file="/WEB-INF/views/_rodape.jspf" %>
