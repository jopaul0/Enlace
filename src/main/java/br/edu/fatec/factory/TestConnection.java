package br.edu.fatec.factory;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("🔍 Testando conexão com o banco...");

        try (Connection connection = ConnectionFactory.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            } else {
                System.out.println("Não foi possível estabelecer a conexão.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao banco:");
            e.printStackTrace();
        }
    }
}
