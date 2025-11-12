package br.edu.fatec.dao;

import br.edu.fatec.enums.MeetStatus;
import br.edu.fatec.model.Meet;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        Gson gson = new Gson();
        while (rs.next()) {
            Meet meet = new Meet();

            meet.setId((long) rs.getInt("id"));
            meet.setAddress(rs.getString("address"));
            meet.setStatus(MeetStatus.valueOf(rs.getString("status")));
            meet.setDate(rs.getObject("birthday", LocalDateTime.class));

            String enlaceJson = rs.getString("enlace");



            meets.add(meet);
        }
        return meets;
    }


}
