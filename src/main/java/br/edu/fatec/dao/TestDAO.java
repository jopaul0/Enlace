package br.edu.fatec.dao;

import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Enlace;
import br.edu.fatec.model.Mother;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) throws SQLException {
//        String json = "[{\"mother\": {\"id\": 1, \"cpf\": \"111111111111\", \"name\": \"claudia\", \"email\": \"pelega@hotmail.com\", \"phone\": \"12992196356\", \"status\": \"inactive\", \"address\": \"Rua dos Elfos, 49\", \"birthday\": \"1983-08-14\"}, \"service\": {\"id\": 1, \"name\": \"Apoio Psicológico\", \"status\": \"active\", \"description\": \"Sessões de suporte emocional e psicológico.\"}}, {\"mother\": {\"id\": 2, \"cpf\": \"111.111.111-11\", \"name\": \"Ana Silva\", \"email\": \"ana.silva@email.com\", \"phone\": \"(11) 98765-4321\", \"status\": \"active\", \"address\": \"Rua das Flores, 100\", \"birthday\": \"1990-05-15\"}, \"service\": {\"id\": 1, \"name\": \"Apoio Psicológico\", \"status\": \"active\", \"description\": \"Sessões de suporte emocional e psicológico.\"}}]";
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
//                        (jsonEl, type, ctx) -> LocalDate.parse(jsonEl.getAsJsonPrimitive().getAsString()))
//                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
//                        (src, type, ctx) -> new JsonPrimitive(src.toString()))
//                .create();
//
//        Type listType = new TypeToken<List<Enlace>>() {
//        }.getType();
//        List<Enlace> enlaces = gson.fromJson(json, listType);
//
//        for (Enlace enlace : enlaces) {
//            System.out.println("------------------");
//            System.out.println(enlace.getMother().toString());
//            System.out.println("+");
//            System.out.println(enlace.getService().toString());
//            System.out.println("------------------");

        MeetDAO dao = new MeetDAO();

        System.out.println(dao.findById(2));
    }
}
