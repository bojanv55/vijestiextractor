package me.vukas;

import javax.persistence.*;

@Entity
@Table(name = "rijeci")
public class Rijec {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String rijec;
    private Integer pojavljivanja;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRijec() {
        return rijec;
    }

    public void setRijec(String rijec) {
        this.rijec = rijec;
    }

    public Integer getPojavljivanja() {
        return pojavljivanja;
    }

    public void setPojavljivanja(Integer pojavljivanja) {
        this.pojavljivanja = pojavljivanja;
    }
}
