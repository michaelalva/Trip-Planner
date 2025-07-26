package com.tco.misc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Select {

    private static final Logger log = LoggerFactory.getLogger(Select.class);

    public static final String TABLE = "cities";
    public static final String COLUMN = "city";
    public static final String COLUMNS = "city,city_ascii,city_alt,lat,lng,country,admin_name,admin_name_ascii,admin_type,capital,population,population_proper";

    public static String near(double latitude, double longitude, int limit) {
        log.info("Finding cities near lat: {}, lng: {}", latitude, longitude);
        String where = String.format("WHERE lat BETWEEN %.4f AND %.4f AND lng BETWEEN %.4f AND %.4f",
                latitude - 1.0, latitude + 1.0, longitude - 1.0, longitude + 1.0);
        return statement(where, COLUMNS + " ", "LIMIT " + limit);
    }

    public static String found(String match) {
        String where = "WHERE " + COLUMN + " LIKE \"%" + escape(match) + "%\"";
        return statement(where, "COUNT(*) AS count", "");
    }

    public static String match(String match, int limit) {
        String where = "WHERE " + COLUMN + " LIKE \"%" + escape(match) + "%\"";
        return statement(where, COLUMNS, "LIMIT " + limit);
    }

    private static String statement(String where, String data, String limit) {
        return String.format("SELECT %s FROM %s %s %s;", data, TABLE, where, limit);
    }

    private static String escape(String input) {
        return input.replace("\"", "\\\"");
    }
}
