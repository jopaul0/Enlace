package br.edu.fatec.dao;

import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Mother;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MotherDAO extends DataDAO<Mother> {

    @Override
    protected String getTableName() {
        return "mothers";
    }

    @Override
    protected List<Mother> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Mother> mothers = new ArrayList<>();
        while (rs.next()) {
            Mother mother = new Mother();

            mother.setId((long) rs.getInt("id"));
            mother.setName(rs.getString("name"));
            mother.setPhone(rs.getString("phone"));
            mother.setCpf(rs.getString("cpf"));
            mother.setEmail(rs.getString("email"));

            mother.setStatus(DefaultStatus.valueOf(rs.getString("status")));
            mother.setBirthday(rs.getObject("birthday", LocalDate.class));


            mothers.add(mother);
        }
        return mothers;
    }

    public void insert(Mother mother) throws SQLException{
        String sql = "insert into mothers (name,cpf,email,phone,address,birthday,status) values (?,?,?,?,?,?,?);";
        executeUpdate(sql, mother.getName(), mother.getCpf(), mother.getEmail(), mother.getPhone(),mother.getAddress(), mother.getBirthday(), mother.getStatus().name());
    }

    public void update(Mother mother) throws SQLException{
        String sql = "update mothers set name = ?, cpf = ?, email = ?, phone = ?, address = ?, birthday = ?, status = ? where id = ?;";
        executeUpdate(sql, mother.getName(), mother.getCpf(), mother.getEmail(), mother.getPhone(),mother.getAddress(),mother.getBirthday(), mother.getStatus().name(), mother.getId());
    }

    public void delete(Mother mother) throws SQLException{
        String sql = "delete from mothers where id = ?";
        executeUpdate(sql, mother.getId());
    }

    public List<Mother> findAllActives() throws  SQLException{
        String sql = "SELECT * FROM mothers where status = 'active'";
        return executeQuery(sql);
    }

    public List<Mother> findBirthdayMother() throws  SQLException{
        String sql = "SELECT * FROM mothers WHERE MONTH(birthday) = MONTH(NOW())";
        return executeQuery(sql);
    }

    public void softDelete(Mother mother) throws SQLException{
        String sql = "update mothers set status='inactive' where id = ?";
        executeUpdate(sql, mother.getId());
    }

}
