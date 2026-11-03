# Habit Tracker - Incremento 1 (Sprint 1)

Projeto do grupo **Verdão** - Métodos Ágeis e IHC (UMC, Engenharia de Software).

## O que o Incremento 1 entrega

| User Story | Funcionalidade | Rota |
|---|---|---|
| US06 | Cadastro de usuário | `/cadastro` |
| US07 | Login e logout | `/login`, `/logout` |
| US01 | Cadastrar hábito | `/habitos/novo` |
| US02 | Listar hábitos do usuário logado | `/habitos` |

Regras de negócio: RN03 (e-mail único), RN04 (senha guardada como hash PBKDF2), RN05 (cada usuário só vê os próprios hábitos).

## Tecnologias

Java 11+ - Servlets e JSP (padrão MVC + DAO) - JDBC - MySQL 8 - Maven - JUnit 5 - Tomcat 9

## Como rodar

### 1. Criar o banco

No terminal, dentro da pasta do projeto:

```
mysql -u root -p < sql/schema.sql
```

### 2. Conferir usuário e senha do MySQL

Abra `src/main/java/br/com/habittracker/dao/ConnectionFactory.java` e ajuste `USUARIO` e `SENHA`
(ou defina as variáveis de ambiente `HT_DB_USER` e `HT_DB_PASSWORD`).

### 3. Subir a aplicação (NetBeans)

1. File > Open Project > escolha a pasta `habit-tracker` (o NetBeans reconhece o projeto Maven).
2. Clique com o botão direito no projeto > Properties > Run > Server: **Apache Tomcat 9**.
3. Clique em Run. O navegador abre em `http://localhost:8080/habit-tracker/`.

Se o seu Tomcat for a versão 10 ou mais nova, troque `javax.servlet` por `jakarta.servlet` nos arquivos `.java`
e use a dependência `jakarta.servlet-api` 6.0.0 no `pom.xml`.

### 4. Rodar os testes unitários

```
mvn test
```

## Estrutura

```
src/main/java/br/com/habittracker/
    model/        Usuario, Habito
    dao/          ConnectionFactory, UsuarioDAO, HabitoDAO
    controller/   Servlets (cadastro, login, logout, habitos, habitos/novo)
    filter/       AutenticacaoFilter (protege as telas internas), EncodingFilter (UTF-8)
    util/         SenhaUtil (hash), Validador (regras dos formulários), Html, Sessao
src/main/webapp/
    WEB-INF/views/   telas JSP (não são acessíveis direto pelo navegador)
    css/style.css
sql/schema.sql
```
