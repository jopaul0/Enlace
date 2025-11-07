package br.edu.fatec.model;

public class TestLombok {
    public static void main(String[] args) {
        Mother mae = new Mother();

        mae.setName("Claudia");
        mae.setId(1L);
        mae.setEmail("pelega@hotmail.com");
        mae.setPhone("12992196356");

        System.out.println(mae.toString());
    }
}
