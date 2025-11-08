package br.edu.fatec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static br.edu.fatec.factory.ConnectionFactory.getConnection;

public abstract class DataDAO<T> {

    // Abstracts
    protected abstract List<T> mapResultSetToList(ResultSet rs) throws SQLException;
    protected abstract String getTableName();


    protected void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);
            ps.executeUpdate();
        }
    }

    protected List<T> executeQuery(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                return mapResultSetToList(rs);
            }
        }
    }

    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public T findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        List<T> results = executeQuery(sql, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<T> findAll() throws  SQLException{
        String sql = "SELECT * FROM " + getTableName();
        return executeQuery(sql);
    }

    public void deleteById(int id) throws SQLException{
        String sql = "DELETE FROM " + getTableName() + "WHERE id = ?";
        executeUpdate(sql, id);
    }
}