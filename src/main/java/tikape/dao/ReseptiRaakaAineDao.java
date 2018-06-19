/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tikape.database.Database;
import tikape.domain.ReseptiRaakaAine;

/**
 *
 * @author User
 */
public class ReseptiRaakaAineDao {
    
    private Database db;
    
    public ReseptiRaakaAineDao(Database database) {
        this.db = database;
    }
    
    public List<ReseptiRaakaAine> findRaakaAineet(Integer key) throws SQLException {
        
        Connection conn = db.getConnection();
        
        PreparedStatement stmt
                    = conn.prepareStatement("SELECT RaakaAine.nimi AS nimi, ReseptiRaakaAine.jarjestys AS jarjestys, "
                            + "ReseptiRaakaAine.maara AS maara, ReseptiRaakaAine.ohje AS ohje "
                            + "FROM ReseptiRaakaAine, RaakaAine "
                            + "WHERE ReseptiRaakaAine.resepti_id = ? "
                            + "AND ReseptiRaakaAine.raaka_aine_id = RaakaAine.id "
                            + "ORDER BY ReseptiRaakaAine.jarjestys");
        stmt.setInt(1,key);
        ResultSet rs = stmt.executeQuery();
            
        List<ReseptiRaakaAine> raakaAineet = new ArrayList<>();
            
        while(rs.next()) {
            String nimi = rs.getString("nimi");
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
            raakaAineet.add(new ReseptiRaakaAine(-1, -1, nimi, jarjestys, maara, ohje));
        }
        
        conn.close();
        
        return raakaAineet;
    }
    
    public void save(ReseptiRaakaAine aine) throws SQLException {
        if(aine.getRaakaAineId() == null || aine.getReseptiId() == null || aine.getJarjestys() == null || aine.getMaara() == null) {
            return;
        }
        
        Connection conn = db.getConnection();
        
        PreparedStatement test = conn.prepareStatement("SELECT * FROM ReseptiRaakaAine "
                + "WHERE raaka_aine_id = ? AND resepti_id = ? ");
        test.setInt(1, aine.getRaakaAineId());
        test.setInt(2, aine.getReseptiId());
        ResultSet rs = test.executeQuery();
        
        if(rs.next()) {
            return;
        }
        
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO ReseptiRaakaAine "
                + "(raaka_aine_id, resepti_id, jarjestys, maara, ohje) VALUES "
                + "(?, ?, ?, ?, ?)");
        stmt.setInt(1, aine.getRaakaAineId());
        stmt.setInt(2, aine.getReseptiId());
        stmt.setInt(3, aine.getJarjestys());
        stmt.setString(4, aine.getMaara());
        stmt.setString(5, aine.getOhje());
        
        stmt.executeUpdate();
        
        conn.close();
    }
    
    public int montakoReseptia(Integer key) throws SQLException {
        int resepteja = 0;
        
        Connection conn = db.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(resepti_id) AS resepteja FROM ReseptiRaakaAine "
                + "WHERE raaka_aine_id = ? ");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            resepteja = rs.getInt("resepteja");
        }
        
        rs.close();
        conn.close();
        
        return resepteja;
    }
    
    public void deleteByReseptiId(Integer key) throws SQLException {
        // avaa yhteys tietokantaan
        Connection conn = db.getConnection();
            
        // tee kysely
        PreparedStatement stmt
            = conn.prepareStatement("DELETE FROM ReseptiRaakaAine WHERE resepti_id = ?");
        stmt.setInt(1, key);
            
        stmt.executeUpdate();
            
        // sulje yhteys tietokantaan
        conn.close();    
    }
    
    public void deleteByRaakaAineId(Integer key) throws SQLException {
        // avaa yhteys tietokantaan
        Connection conn = db.getConnection();
            
        // tee kysely
        PreparedStatement stmt
            = conn.prepareStatement("DELETE FROM ReseptiRaakaAine WHERE raaka_aine_id = ?");
        stmt.setInt(1, key);
            
        stmt.executeUpdate();
            
        // sulje yhteys tietokantaan
        conn.close(); 
    }
}
