/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.reseptiarkisto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.database.Database;
import tikape.domain.RaakaAine;

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
        
        
        // Reseptilistauksen näyttö
        Spark.get("/", (req,res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        
        // Raaka-ainelistauksen näyttö
        Spark.get("/raaka-aineet/", (req,res) -> {
            List<RaakaAine> raakaAineet = new ArrayList<>();
            
            Connection conn = db.getConnection();
            
             // tee kysely
            PreparedStatement stmt
                    = conn.prepareStatement("SELECT id, nimi FROM RaakaAine");
            ResultSet tulos = stmt.executeQuery();

            // käsittele kyselyn tulokset
            while (tulos.next()) {
                String nimi = tulos.getString("nimi");
                Integer id = tulos.getInt("id");
                raakaAineet.add(new RaakaAine(id,nimi));
            }
            // sulje yhteys tietokantaan
            conn.close();
            
            HashMap map = new HashMap<>();
            
            map.put("raakaAineet", raakaAineet);
            
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
        
        
        // Raaka-aineen lisäys
        Spark.post("/raaka-aineet/", (req, res) -> {
            Connection conn = db.getConnection();
            
            // tee kysely
            PreparedStatement stmt
                    = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            stmt.setString(1, req.queryParams("aine"));

            stmt.executeUpdate();

            // sulje yhteys tietokantaan
            conn.close();

            res.redirect("/raaka-aineet/");
            
            return "";
        });
        
    }
}
