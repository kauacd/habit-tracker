# Habit Tracker - Incremento 2 (Sprint 2)

Projeto do grupo **Verdão** - Métodos Ágeis e IHC (UMC, Engenharia de Software).

O Incremento 2 inclui tudo do Incremento 1 e acrescenta a **US03 - Marcar hábito como concluído**.

## O que o Incremento 2 entrega

| US | Funcionalidade | Onde |
|---|---|---|
| US03 | Marcar hábito como concluído hoje | Botão redondo em cada cartão de "Meus Hábitos" |

- **RN01:** no máximo uma conclusão por hábito por dia (regra no serviço + `UNIQUE` no banco).
- **RN02:** a conclusão fica gravada com a data (fuso America/Sao_Paulo).
- **RN05:** só o dono do hábito consegue concluí-lo.
- **Feedback imediato:** a marca aparece na hora (JavaScript) e o pedido vai ao servidor em segundo plano. Se der erro, desfaz e avisa. Sem JavaScript, o formulário também funciona.

## Testar sem derrubar o Incremento 1 (Tomcat ligado)

O Incremento 2 sobe como outra aplicação no mesmo Tomcat: `/habit-tracker-v2`. O Incremento 1 continua em `/habit-tracker`.

**1. Criar a tabela nova (uma vez)** - só ACRESCENTA a tabela `conclusoes`, o Incremento 1 não é afetado:

```
mysql -u ht -pht123 habit_tracker < sql/incremento2.sql
```

(ou abra `sql/incremento2.sql` no MySQL Workbench e clique no raio)

**2. Gerar o .war:** no NetBeans, botão direito no projeto > **Clean and Build**.

**3. Copiar para o Tomcat ligado:**

```
./copiar-para-tomcat.sh
```

**4. Abrir:** `http://localhost:8080/habit-tracker-v2/` (as contas e os hábitos do Incremento 1 já funcionam aqui).

Para testar uma mudança: faça **Clean and Build** de novo e rode o script. O Tomcat recarrega sozinho em poucos segundos.

## Tecnologias

Java 11+ - Servlets e JSP (MVC + DAO + Service) - JDBC - MySQL 8 - Maven - JUnit 5 - Tomcat 9

Usuário e senha do banco: `ht` / `ht123` (em `ConnectionFactory.java` ou pelas variáveis `HT_DB_USER` e `HT_DB_PASSWORD`).

## Testes unitários

```
mvn test
```

25 testes: validações, hash de senha, escape de HTML e a regra de conclusão (`ConclusaoServiceTest`: RN01, RN02, RN05).

## O que é novo neste incremento

```
dao/ConclusaoRepositorio.java      interface com o que a regra precisa do banco
dao/ConclusaoDAO.java              implementação em JDBC
service/ConclusaoService.java      regra de negócio (RN01, RN02, RN05)
controller/ConcluirHabitoServlet   POST /habitos/concluir
webapp/js/app.js                   feedback imediato
sql/incremento2.sql                tabela conclusoes
```
