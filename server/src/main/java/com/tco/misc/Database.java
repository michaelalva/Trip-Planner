package com.tco.misc;

import com.tco.requests.Place;
import com.tco.requests.Places;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Database {

    private final static String COLUMNS = "city,city_ascii,city_alt,lat,lng,country,admin_name,admin_name_ascii,admin_type,capital,population,population_proper";

    private static final Logger log = LoggerFactory.getLogger(Database.class);

    static Integer found(String sql) throws Exception {

            try (
                  // connect to the database and query
                  Connection conn = DriverManager.getConnection(Credentials.URL, Credentials.USER,
                        Credentials.PASSWORD);
                  Statement query = conn.createStatement();
                  ResultSet results = query.executeQuery(sql)
            ) {
                return count(results);
            } catch (Exception e) {
                throw e;
            }
        }

        private static Integer count(ResultSet results) throws Exception {
            if (results.next()) {
                return results.getInt("count");
            }
            throw new Exception("No count results in found query.");
        }


        static Places places(String sql, Integer limit) throws Exception {
            String url = Credentials.URL;
            String user = Credentials.USER;
            String password = Credentials.PASSWORD;
            try (
                  // connect to the database and query
                  Connection conn = DriverManager.getConnection(url, user, password);
                  Statement query = conn.createStatement();
                  ResultSet results = query.executeQuery(sql)
            ) {
                return convertQueryResultsToPlaces(results, COLUMNS);
            } catch (Exception e) {
                throw e;
            }
        }


        private static Places convertQueryResultsToPlaces(ResultSet results, String columns)
              throws Exception {
            int count = 0;
            String[] cols = columns.split(",");
            Places places = new Places();
            while (results.next()) {
                Place place = new Place();
                List<String> keywords = new ArrayList<>();
                for (String col : cols) {
                    String mappedCol = map(col);
                    if (mappedCol.equals("keywords")) {
                        keywords.add(results.getString(col));
                    } else {
                        place.put(mappedCol, results.getString(col));
                    }
                    log.info("col: {}, value: {}", mappedCol, results.getString(col));
                }
                place.put(String.format("keywords", "%s_keywords"), String.join(",", keywords));
                place.put("index", String.format("%d", ++count));
                places.add(place);
            }
            return places;
        }

        private static String map(String col) throws Exception {
            switch (col) {
                case "city":
                    return "name";
                case "city_ascii":
                    return "keywords";
                case "city_alt":
                    return "keywords";
                case "lat":
                    return "latitude";
                case "lng":
                    return "longitude";
                case "country":
                    return "country";
                case "admin_name":
                    return "region";
                case "admin_name_ascii":
                    return "keywords";
                case "capital":
                    return "capitalType";
                case "population":
                    return "population_false";
                case "population_proper":
                    return "population";
                case "timezone":
                    return "timezone";
                case "admin_type":
                    return "regionType";
                default:
                    throw new Exception("Unknown column: "+col);
            }
        }

}
