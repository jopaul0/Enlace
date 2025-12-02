package br.edu.fatec.util;

import java.time.LocalDate;

public class TestUtils {
    public static void main(String[] args) {

    System.out.println("--- Testando LocalDate -> String BR ---");
    LocalDate dataObj = LocalDate.of(2025, 11, 9);
    String dataFormatada = DateUtils.formatDate(dataObj);
    System.out.println("Resultado: " + dataFormatada);

    System.out.println("\n--- Testando String BR -> String ISO ---");
    String dataStringBR = "09/11/2025";
    LocalDate dataDesformatada = DateUtils.formatIsoDate(dataStringBR);
    System.out.println("Resultado: " + dataDesformatada); // Saída: 2025-11-09
}
}
