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
import tikape.domain.RaakaAine;

/**
 *
 * @author User
 */
public class RaakaAineDao implements Dao<RaakaAine, Integer>{
    
    private Database db;
    
    public RaakaAineDao(Database database) {
        this.db = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> raakaAineet = new ArrayList<>();
            
        Connection conn = db.getConnection();
            
        // tee kysely
        PreparedStatement stmt
           = conn.prepareStatement("SELECT id, nimi FROM RaakaAine ORDER BY nimi");
        ResultSet tulos = stmt.executeQuery();

        // käsittele kyselyn tulokset
        while (tulos.next()) {
            String nimi = tulos.getString("nimi");
            Integer id = tulos.getInt("id");
            raakaAineet.add(new RaakaAine(id,nimi));
        }
        // sulje yhteys tietokantaan
        conn.close();    
    
        return raakaAineet;
    }

    @Override
    public RaakaAine save(RaakaAine object) throws SQLException {
        // Tarkistetaan ettei nimi ole null tai tyhjä.
        if(object.getNimi() == null || object.getNimi().equals("")) {
            return null;
        }
        
        
        // Tarkistetaan ettei raaka-aine jo löydy tietokannasta.
        Connection conn = db.getConnection();
        
        PreparedStatement testStmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        testStmt.setString(1, object.getNimi());
        ResultSet rs = testStmt.executeQuery();
        
        if(rs.next()) {
            return null;
        }
        // tee kysely
        PreparedStatement stmt
           = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
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
            = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
            
        stmt.executeUpdate();
            
        // sulje yhteys tietokantaan
        conn.close();    
    }
    
}
