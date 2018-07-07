package me.vukas.webflux;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Osoba {
    @Id
    private Integer id;
    private String ime;
    private String prezime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
}
