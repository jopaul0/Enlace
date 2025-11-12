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
        String json = "[{\"mother\": {\"id\": 1, \"name\": \"claudia\", \"email\": \"pelega@hotmail.com\", \"phone\": \"12992196356\"}, \"service\": {\"id\": 1, \"name\": \"Apoio Psicológico\", \"description\": \"Sessões de suporte emocional e psicológico.\"}, \"id\": 1}, {\"mother\": {\"id\": 2, \"name\": \"Ana Silva\", \"email\": \"ana.silva@email.com\", \"phone\": \"(11) 98765-4321\"}, \"service\": {\"id\": 1, \"name\": \"Apoio Psicológico\", \"description\": \"Sessões de suporte emocional e psicológico.\"}, \"id\": 1}]";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (jsonEl, type, ctx) -> LocalDate.parse(jsonEl.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (src, type, ctx) -> new JsonPrimitive(src.toString()))
                .create();

        Type listType = new TypeToken<List<Enlace>>() {
        }.getType();
        List<Enlace> enlaces = gson.fromJson(json, listType);

        for (Enlace enlace : enlaces) {
            System.out.println("------------------");
            System.out.println(enlace.getMother().toString());
            System.out.println("+");
            System.out.println(enlace.getService().toString());
            System.out.println("------------------");
        }
    }
}
