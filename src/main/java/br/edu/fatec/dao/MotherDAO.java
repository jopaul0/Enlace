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
}
