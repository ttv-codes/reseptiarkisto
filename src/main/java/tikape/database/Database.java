/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class Database {
    
    private String address;
    
    public Database(String databaseAddress) {
        this.address = databaseAddress;
    }
    
    public Connection getConnection() throws SQLException {
        if (address != null && address.length() > 0) {
            return DriverManager.getConnection(address);
        }
        
        return DriverManager.getConnection("jdbc:sqlite:reseptiarkisto.db");
    }
}
