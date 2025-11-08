package br.edu.fatec.dao;

import br.edu.fatec.model.Mother;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            mother.setName(rs.getString("nome"));
            mothers.add(mother);
        }
        return mothers;
    }

    public void insert(Mother mother) throws SQLException{
        String sql = "insert into mothers (name,cpf,email,phone) values (?,?,?,?);";
        executeUpdate(sql, mother.getName(), mother.getCpf(), mother.getEmail(), mother.getPhone());
    }

    public void update(Mother mother) throws SQLException{
        String sql = "update mothers set name = ?, cpf = ?, email = ?, phone = ? where id = ?;";
        executeUpdate(sql, mother.getName(), mother.getCpf(), mother.getEmail(), mother.getPhone(), mother.getId());
    }

    public void delete(Mother mother) throws SQLException{
        String sql = "delete from mothers where id = ?";
        executeUpdate(sql, mother.getId());
    }



}
