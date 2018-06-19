/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.domain;

/**
 *
 * @author User
 */
public class RaakaAine {
    
    private Integer id;
    private String nimi;
    
    public RaakaAine(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    @Override
    public String toString() {
        return this.nimi;
    }
    
}
