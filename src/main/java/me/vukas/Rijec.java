package me.vukas;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rijeci")
@Cacheable
public class Rijec {
    @Id
    private String rijec;   //posto je unicode mora da bude collation utf8mb4_unicode_ci
    private Integer pojavljivanja;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rijec rijec1 = (Rijec)o;
        return Objects.equals(rijec, rijec1.rijec);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rijec);
    }
}
