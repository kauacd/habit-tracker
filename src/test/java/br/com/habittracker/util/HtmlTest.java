package br.com.habittracker.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlTest {

    @Test
    void escapaCaracteresPerigosParaEvitarXss() {
        assertEquals("&lt;script&gt;alert(&quot;x&quot;)&lt;/script&gt;", Html.esc("<script>alert(\"x\")</script>"));
    }

    @Test
    void nuloViraTextoVazio() {
        assertEquals("", Html.esc(null));
    }
}
