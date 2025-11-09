package br.edu.fatec.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateUtils() {}

    public static String formatDate(LocalDate date){
        try{
            return date.format(DATE_FORMAT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDate formatIsoDate(String formatDate){
        try{
            return LocalDate.parse(formatDate, DATE_FORMAT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
