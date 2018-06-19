/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.reseptiarkisto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.dao.RaakaAineDao;
import tikape.dao.ReseptiDao;
import tikape.dao.ReseptiRaakaAineDao;
import tikape.database.Database;
import tikape.domain.RaakaAine;
import tikape.domain.Resepti;
import tikape.domain.ReseptiRaakaAine;

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
        ReseptiDao reseptiDao = new ReseptiDao(db);
        RaakaAineDao raakaAineDao = new RaakaAineDao(db);
        ReseptiRaakaAineDao reseptiRaakaAineDao = new ReseptiRaakaAineDao(db);
        
        
        // Reseptilistauksen näyttö---------------------------------------------
        Spark.get("/", (req,res) -> {
            List<Resepti> reseptit = reseptiDao.findAll();
            
            HashMap map = new HashMap<>();
            
            map.put("reseptit",reseptit);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        
        
        // Reseptilisäyksen näyttö----------------------------------------------
        Spark.get("/reseptit/uusi/", (req, res) -> {
            List<Resepti> reseptit = reseptiDao.findAll();
            List<RaakaAine> raakaAineet = raakaAineDao.findAll();
            
            HashMap map = new HashMap<>();
            
            map.put("reseptit",reseptit);
            map.put("raakaAineet",raakaAineet);

            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());
        
        
        // Reseptin lisäys listaukseen------------------------------------------
        Spark.post("/reseptit/uusi/lisaa-resepti/", (req, res) -> {
            reseptiDao.save(new Resepti(-1, req.queryParams("resepti")));
            
            res.redirect("/reseptit/uusi/");
            
            return "";
        });
        
        // Reseptin poisto------------------------------------------------------
        Spark.get("/reseptit/:reseptiId/delete", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":reseptiId"));
            reseptiRaakaAineDao.deleteByReseptiId(id);
            reseptiDao.delete(id);
            
            res.redirect("/reseptit/uusi/");
            
            return "";
        });
        
        // Raaka-aineiden lisäys reseptiin -------------------------------------
        Spark.post("/reseptit/uusi/lisaa-raaka-aine/", (req, res) -> {
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaAineId"));
            System.out.println("tulostetaan" + raakaAineId);
            Integer reseptiId = Integer.parseInt(req.queryParams("reseptiId"));
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            
            reseptiRaakaAineDao.save(new ReseptiRaakaAine(raakaAineId, reseptiId, "placeholder", jarjestys,
                    maara, ohje));
            
            res.redirect("/reseptit/uusi/");
            
            return "";
        });
        
        
        // Reseptin näyttö------------------------------------------------------
        Spark.get("reseptit/:id", (req, res) -> {
            Resepti resepti = reseptiDao.findOne(Integer.parseInt(req.params(":id"))); 
            
            List<ReseptiRaakaAine> raakaAineet = reseptiRaakaAineDao.findRaakaAineet(resepti.getId());
            
            HashMap map = new HashMap<>();
            map.put("resepti",resepti);
            map.put("reseptinRaakaAineet", raakaAineet);

            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine());
        
        
        // Raaka-ainelistauksen näyttö------------------------------------------
        Spark.get("/raaka-aineet/", (req,res) -> {
            List<RaakaAine> raakaAineet = raakaAineDao.findAll();
            HashMap<RaakaAine, Integer> raakaAineetJaLukumaarat = new HashMap<>();
            
            Integer maxResepteja = 0;
            Integer minResepteja = 10000;
            
            for(RaakaAine aine : raakaAineet) {
                Integer resepteja = reseptiRaakaAineDao.montakoReseptia(aine.getId());
                if (resepteja < minResepteja) {
                    minResepteja = resepteja;
                }
                if (resepteja > maxResepteja) {
                    maxResepteja = resepteja;
                }
                raakaAineetJaLukumaarat.put(aine,resepteja);
            }
            
            List<RaakaAine> suosituimmat = new ArrayList<>();
            List<RaakaAine> harvinaisimmat = new ArrayList<>();
            
            for(RaakaAine aine : raakaAineet) {
                Integer resepteja = raakaAineetJaLukumaarat.get(aine);
                if (resepteja.equals(maxResepteja)) {
                    suosituimmat.add(aine);
                }
                if (resepteja.equals(minResepteja)) {
                    harvinaisimmat.add(aine);
                }
            }
            
            HashMap map = new HashMap<>();
            
            map.put("raakaAineet", raakaAineet);
            map.put("lukumaarat", raakaAineetJaLukumaarat);
            map.put("suosituimmat", suosituimmat);
            map.put("harvinaisimmat", harvinaisimmat);
            map.put("suositutlkm", maxResepteja);
            map.put("harvinaisetlkm", minResepteja);
            
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
        
        
        // Raaka-aineen lisäys--------------------------------------------------
        Spark.post("/raaka-aineet/", (req, res) -> {
            raakaAineDao.save(new RaakaAine(-1,req.queryParams("aine")));

            res.redirect("/raaka-aineet/");
            
            return "";
        });
        
        // Raaka-aineen poisto--------------------------------------------------
        Spark.get("/raaka-aineet/:raakaAineId/delete", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":raakaAineId"));
            reseptiRaakaAineDao.deleteByRaakaAineId(id);
            raakaAineDao.delete(id);

            
            res.redirect("/raaka-aineet/");
            
            return "";
        });
        
    }
}
