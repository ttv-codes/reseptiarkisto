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
import java.util.List;
import tikape.database.Database;
import tikape.domain.Resepti;

/**
 *
 * @author User
 */
public class ReseptiDao implements Dao<Resepti, Integer> {

    private Database db;
    
    public ReseptiDao(Database database) {
        this.db = database;
    }

    @Override
    public Resepti findOne(Integer key) throws SQLException {
        String reseptinNimi = "";
            
        Connection conn = db.getConnection();
            
        PreparedStatement stmt
            = conn.prepareStatement("SELECT nimi FROM Resepti WHERE id = ?");
        stmt.setInt(1,key);
        ResultSet rs = stmt.executeQuery();
            
        while(rs.next()) {
            reseptinNimi = rs.getString("nimi");
        }
        
        conn.close();
        
        return new Resepti(key,reseptinNimi);
    }

    @Override
    public Resepti save(Resepti object) throws SQLException {
        if(object.getNimi() == null || object.getNimi().equals("")) {
            return null;
        }
        
        // Tarkistetaan ettei raaka-aine jo löydy tietokannasta.
        Connection conn = db.getConnection();
        
        PreparedStatement testStmt = conn.prepareStatement("SELECT * FROM Resepti WHERE nimi = ?");
        testStmt.setString(1, object.getNimi());
        ResultSet rs = testStmt.executeQuery();
        
        if(rs.next()) {
            return null;
        }
        // tee kysely
        PreparedStatement stmt
           = conn.prepareStatement("INSERT INTO Resepti (nimi) VALUES (?)");
        stmt.setString(1, object.getNimi());

        stmt.executeUpdate();

        // sulje yhteys tietokantaan
        conn.close();    
    
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // avaa yhteys tietokantaan
        Connection conn = db.getConnection();
            
        // tee kysely
        PreparedStatement stmt
            = conn.prepareStatement("DELETE FROM Resepti WHERE id = ?");
        stmt.setInt(1, key);
            
        stmt.executeUpdate();
            
        // sulje yhteys tietokantaan
        conn.close();    
    }

    @Override
    public List<Resepti> findAll() throws SQLException {
        List<Resepti> reseptit = new ArrayList<>();   
        
        Connection conn = db.getConnection();
            
        PreparedStatement stmt
               = conn.prepareStatement("SELECT id, nimi FROM Resepti ORDER BY nimi");
        ResultSet rs = stmt.executeQuery();
            
        while(rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            reseptit.add(new Resepti(id,nimi));
        }
            
        conn.close();
        
        return reseptit;
    }
    
}
