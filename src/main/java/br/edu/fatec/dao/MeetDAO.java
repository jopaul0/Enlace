package br.edu.fatec.dao;

import br.edu.fatec.enums.MeetStatus;
import br.edu.fatec.model.Enlace;
import br.edu.fatec.model.Meet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetDAO extends DataDAO<Meet> {

    @Override
    protected String getTableName() {
        return "mothers";
    }

    @Override
    protected List<Meet> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Meet> meets = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (jsonEl, type, ctx) -> LocalDate.parse(jsonEl.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (src, type, ctx) -> new JsonPrimitive(src.toString()))
                .create();

        while (rs.next()) {
            Meet meet = new Meet();

            meet.setId((long) rs.getInt("id"));
            meet.setAddress(rs.getString("address"));
            meet.setStatus(MeetStatus.valueOf(rs.getString("status")));
            meet.setDate(rs.getObject("date", LocalDateTime.class));

            String enlaceJson = rs.getString("enlace");
            Type listType = new TypeToken<List<Enlace>>() {
            }.getType();
            List<Enlace> enlaces = gson.fromJson(enlaceJson, listType);

            meet.setEnlaces(enlaces);

            meets.add(meet);
        }
        return meets;
    }

    @Override
    public List<Meet> findAll() throws  SQLException{
        String sql = "SELECT\n" +
                "    m.id AS id,\n" +
                "    m.date AS date,\n" +
                "    m.address AS address,\n" +
                "    m.status AS status,\n" +
                "\n" +
                "    JSON_ARRAYAGG(\n" +
                "        JSON_OBJECT(\n" +
                "            'service', JSON_OBJECT(\n" +
                "                'id', s.id,\n" +
                "                'name', s.name,\n" +
                "                'description', s.description,\n" +
                "                'status', s.status\n" +
                "            ),\n" +
                "            \n" +
                "            'mother', JSON_OBJECT(\n" +
                "                'id', mo.id,\n" +
                "                'name', mo.name,\n" +
                "                'email', mo.email,\n" +
                "                'phone', mo.phone,\n" +
                "                'cpf', mo.cpf,\n" +
                "                'address', mo.address,\n" +
                "                'birthday', mo.birthday,\n" +
                "                'status', mo.status\n" +
                "            )\n" +
                "        )\n" +
                "    ) AS enlace\n" +
                "FROM\n" +
                "    meets m\n" +
                "JOIN\n" +
                "    enlace e ON m.id = e.idmeets\n" +
                "JOIN\n" +
                "    services s ON e.idservices = s.id\n" +
                "JOIN\n" +
                "    mothers mo ON e.idmothers = mo.id\n" +
                "GROUP BY\n" +
                "    m.id, m.date, m.address, m.status;";
        return executeQuery(sql);
    }

    @Override
    public Meet findById(int id) throws SQLException {
        String sql = "SELECT\n" +
                "    m.id AS id,\n" +
                "    m.date AS date,\n" +
                "    m.address AS address,\n" +
                "    m.status AS status,\n" +
                "\n" +
                "    JSON_ARRAYAGG(\n" +
                "        JSON_OBJECT(\n" +
                "            'service', JSON_OBJECT(\n" +
                "                'id', s.id,\n" +
                "                'name', s.name,\n" +
                "                'description', s.description,\n" +
                "                'status', s.status\n" +
                "            ),\n" +
                "            \n" +
                "            'mother', JSON_OBJECT(\n" +
                "                'id', mo.id,\n" +
                "                'name', mo.name,\n" +
                "                'email', mo.email,\n" +
                "                'phone', mo.phone,\n" +
                "                'cpf', mo.cpf,\n" +
                "                'address', mo.address,\n" +
                "                'birthday', mo.birthday,\n" +
                "                'status', mo.status\n" +
                "            )\n" +
                "        )\n" +
                "    ) AS enlace\n" +
                "FROM\n" +
                "    meets m\n" +
                "JOIN\n" +
                "    enlace e ON m.id = e.idmeets\n" +
                "JOIN\n" +
                "    services s ON e.idservices = s.id\n" +
                "JOIN\n" +
                "    mothers mo ON e.idmothers = mo.id\n" +
                "WHERE\n" +
                "\t m.id = ? \n" +
                "GROUP BY\n" +
                "    m.id, m.date, m.address, m.status;";
        List<Meet> results = executeQuery(sql, id);
        return results.isEmpty() ? null : results.get(0);
    }
}
