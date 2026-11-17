# Habit Tracker - Incremento 3 (Sprint 3 - MVP)

Projeto do grupo **Verdão** - Métodos Ágeis e IHC (UMC, Engenharia de Software).

O Incremento 3 inclui tudo dos Incrementos 1 e 2 e fecha o MVP com **US04** e **US05**, mais ajustes de estabilização.

## O que o Incremento 3 entrega

| US | Funcionalidade | Onde |
|---|---|---|
| US04 | Histórico individual do hábito | Toque no nome do hábito em "Meus Hábitos" (`/habitos/historico?id=...`) |
| US05 | Gráfico de progresso semanal | Aba **Progresso** (`/progresso`) |

- **Cartões de hábito:** agora mostram os últimos 7 dias (bolinhas) e "x/7 dias - y%". Atualiza na hora ao concluir.
- **Histórico:** "6/7 dias - 85%" (sem arredondar para cima), lista dos 7 dias e total de conclusões. Hábito sem nenhuma conclusão mostra "Ainda não há dados suficientes".
- **Progresso:** barras de segunda a domingo (hábitos concluídos por dia / total de hábitos). Dias futuros ficam vazios, e a semana sem conclusões aparece zerada, sem erro.
- **RN05:** histórico só abre para o dono do hábito (senão 404).
- **Estabilização:**
  - erro inesperado (ex.: banco fora do ar) mostra uma tela amigável, e o detalhe vai para o log do servidor;
  - barra de abas **Hábitos | Progresso**;
  - só calcula com datas do fuso America/Sao_Paulo.

## Testar sem derrubar os Incrementos 1 e 2 (Tomcat ligado)

Este incremento sobe como outra aplicação: **/habit-tracker-v3**. Os outros continuam em `/habit-tracker` e `/habit-tracker-v2`.

**1. Banco:** NÃO precisa de SQL novo. Ele usa as tabelas `usuarios`, `habitos` e `conclusoes` (a `conclusoes` vem do `sql/incremento2.sql` do Incremento 2).

**2. Gerar o .war:** no NetBeans, botão direito no projeto > **Clean and Build**.

**3. Copiar para o Tomcat ligado:**

```
./copiar-para-tomcat.sh
```

**4. Abrir (espere uns 5 a 10 segundos):** `http://localhost:8080/habit-tracker-v3/`

As contas e hábitos dos incrementos anteriores já funcionam aqui.

## Dados de exemplo (opcional)

Para ver o gráfico e o histórico "cheios" sem esperar vários dias, abra `sql/dados-exemplo.sql`, troque o e-mail pelo da sua conta e execute. São conclusões **fictícias** nos 6 dias anteriores (o arquivo explica como apagar). Não use como evidência de uso real.

## Tecnologias

Java 11+ - Servlets e JSP (MVC + DAO + Service) - JDBC - MySQL 8 - Maven - JUnit 5 - Tomcat 9

Usuário e senha do banco: `ht` / `ht123` (em `ConnectionFactory.java` ou nas variáveis `HT_DB_USER` e `HT_DB_PASSWORD`).

## Testes unitários

```
mvn test
```

42 testes: validações, hash de senha, escape de HTML, datas em português, regra de conclusão (RN01, RN02, RN05) e as regras de histórico e progresso (`ProgressoServiceTest`).

## O que é novo neste incremento

```
dao/ProgressoRepositorio.java + ProgressoDAO.java    consultas de leitura
service/ProgressoService.java                        regras de histórico (US04) e semana (US05)
service/Resumo7Dias, HistoricoHabito, ProgressoSemanal, DiaSemana, DiaHabito   dados para as telas
controller/HistoricoServlet.java, ProgressoServlet.java
filter/ErroFilter.java                               tela amigável para erros inesperados
util/Datas.java                                      dias da semana em português
views: historico.jsp, progresso.jsp, erro.jsp, _nav.jspf
```
