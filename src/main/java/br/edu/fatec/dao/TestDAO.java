package br.edu.fatec.dao;

import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Mother;

import java.sql.SQLException;
import java.time.LocalDate;

public class TestDAO {
    public static void main(String[] args) throws SQLException {
        Mother mae = new Mother();
        mae.setId((long) 1);
        mae.setName("claudia");
        mae.setCpf("111111111111");
        mae.setEmail("pelega@hotmail.com");
        mae.setPhone("12992196356");
        mae.setBirthday(LocalDate.of(1983, 8, 14));
        mae.setStatus(DefaultStatus.inactive);

        MotherDAO maedao = new MotherDAO();

        maedao.delete(mae);
    }
}
