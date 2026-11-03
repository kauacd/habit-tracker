package br.com.habittracker.util;

import java.util.Map;

/** Auxiliares para as páginas JSP. */
public class Html {

    private Html() { }

    /** Escapa texto antes de imprimir no HTML (evita XSS). */
    public static String esc(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(texto.length() + 16);
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '&': sb.append("&amp;"); break;
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '"': sb.append("&quot;"); break;
                case '\'': sb.append("&#39;"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Parágrafo com a mensagem de erro do campo (ou texto vazio se não houver erro). */
    public static String erroCampo(Map<String, String> erros, String campo) {
        if (erros == null || !erros.containsKey(campo)) {
            return "";
        }
        return "<p class=\"erro\" id=\"erro-" + campo + "\">" + esc(erros.get(campo)) + "</p>";
    }

    /** Atributos de acessibilidade para o input que está com erro. */
    public static String attrsErro(Map<String, String> erros, String campo) {
        if (erros == null || !erros.containsKey(campo)) {
            return "";
        }
        return "aria-invalid=\"true\" aria-describedby=\"erro-" + campo + "\"";
    }
}
