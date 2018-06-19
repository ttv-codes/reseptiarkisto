/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.dao;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author User
 */
public interface Dao<T, K> {
    T findOne(K key) throws SQLException;
    List<T> findAll() throws SQLException;
    T save(T object) throws SQLException;
    void delete(K key) throws SQLException;
}
