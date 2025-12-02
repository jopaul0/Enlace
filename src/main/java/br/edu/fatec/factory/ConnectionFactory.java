package br.edu.fatec.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Campos estáticos para armazenar as configurações de conexão
    private static String DB_URL = null;
    private static String DB_USER = null;
    private static String DB_PASSWORD = null;

    private ConnectionFactory() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Define as configurações de conexão, chamando o método pela tela de configuração.
     */
    public static void setConnectionParams(String url, String user, String password) {
        DB_URL = url;
        DB_USER = user;
        DB_PASSWORD = password;
    }

    /**
     * Tenta obter uma conexão com os parâmetros definidos.
     * @return Uma nova conexão JDBC.
     * @throws SQLException se os parâmetros não foram definidos ou a conexão falhar.
     */
    public static Connection getConnection() throws SQLException {
        if (DB_URL == null || DB_USER == null) {
            throw new SQLException("Parâmetros de conexão com o banco de dados não foram definidos. Configure o acesso via tela inicial.");
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Método utilitário para testar a validade das credenciais.
     */
    public static boolean testConnection(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}