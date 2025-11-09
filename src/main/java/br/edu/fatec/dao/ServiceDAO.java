package br.edu.fatec.dao;

import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO extends DataDAO<Service> {

    @Override
    protected String getTableName(){
        return "services";
    }

    @Override
    protected List<Service> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Service> services = new ArrayList<>();
        while (rs.next()) {
            Service service = new Service();

            service.setId((long) rs.getInt("id"));
            service.setName(rs.getString("name"));
            service.setDescription(rs.getString("description"));

            service.setStatus(DefaultStatus.valueOf(rs.getString("status")));

            services.add(service);
        }
        return services;
    }

    public void insert(Service service) throws SQLException{
        String sql = "insert into services (name,description,status) values (?,?,?);";
        executeUpdate(sql, service.getName(), service.getDescription(), service.getStatus().name());
    }

    public void update(Service service) throws SQLException{
        String sql = "update services set name = ?, description = ?, status = ? where id = ?;";
        executeUpdate(sql, service.getName(), service.getDescription(), service.getStatus().name(), service.getId());
    }

    public void delete(Service service) throws SQLException{
        String sql = "delete from services where id = ?;";
        executeUpdate(sql, service.getId());
    }

    public List<Service> findAllActives() throws  SQLException{
        String sql = "SELECT * FROM services where status = 'active'";
        return executeQuery(sql);
    }

    public void softDelete(Service service) throws SQLException{
        String sql = "update services set status='inactive' where id = ?";
        executeUpdate(sql, service.getId());
    }

}
