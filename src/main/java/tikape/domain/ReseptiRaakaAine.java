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
public class ReseptiRaakaAine {
    
    private Integer raakaAineId;
    private Integer reseptiId;
    private String nimi;
    private Integer jarjestys;
    private String maara;
    private String ohje;
    
    public ReseptiRaakaAine(Integer raakaAineId, Integer reseptiId, String raakaAine, Integer jarjestys,
                              String maara, String ohje) {
        this.raakaAineId = raakaAineId;
        this.reseptiId = reseptiId;
        this.nimi = raakaAine;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public Integer getRaakaAineId() {
        return this.raakaAineId;
    }
    
    public Integer getReseptiId() {
        return this.reseptiId;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public Integer getJarjestys() {
        return this.jarjestys;
    }
    
    public String getMaara() {
        return this.maara;
    }
    
    public String getOhje() {
        return this.ohje;
    }
}
