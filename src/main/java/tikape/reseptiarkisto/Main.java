/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.reseptiarkisto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.database.Database;

/**
 *
 * @author User
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        // Haetaan Herokun tietokannan osoite ja tehdään Database-olio sillä osoitteella
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        Database db = new Database(dbUrl);
        
        Spark.get("/", (req,res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/raaka-aineet/", (req,res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
        
    }
}
