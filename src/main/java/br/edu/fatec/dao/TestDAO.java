package br.edu.fatec.dao;

import br.edu.fatec.model.Mother;

import java.sql.SQLException;

public class TestDAO {
    public static void main(String[] args) throws SQLException {
        Mother mae = new Mother();
        mae.setId((long) 1);
        mae.setName("Vanessa");
        mae.setCpf("111111111111");
        mae.setEmail("pelega@hotmail.com");
        mae.setPhone("12992196356");

        MotherDAO maedao = new MotherDAO();

        maedao.delete(mae);
    }
}
