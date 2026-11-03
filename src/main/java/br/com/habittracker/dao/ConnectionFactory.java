package br.com.habittracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Cria conexões com o MySQL.
 *
 * Ajuste os valores padrão abaixo para o seu computador OU defina as variáveis
 * de ambiente HT_DB_URL, HT_DB_USER e HT_DB_PASSWORD (sem mexer no código).
 */
public class ConnectionFactory {

    private static final String URL = valor("HT_DB_URL",
            "jdbc:mysql://localhost:3306/habit_tracker?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
    private static final String USUARIO = valor("HT_DB_USER", "ht");
    private static final String SENHA = valor("HT_DB_PASSWORD", "ht123");

    // Registra o driver do MySQL de forma explícita (necessário quando o driver está dentro do .war no Tomcat)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionFactory() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    private static String valor(String variavel, String padrao) {
        String v = System.getenv(variavel);
        return (v == null || v.isEmpty()) ? padrao : v;
    }
}
