package me.vukas;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "trigram")
@Cacheable
public class Trigram {
	@EmbeddedId
	private TrigramKey key;
	private Integer ponavljanja;

	public TrigramKey getKey() {
		return key;
	}

	public void setKey(TrigramKey key) {
		this.key = key;
	}

	public Integer getPonavljanja() {
		return ponavljanja;
	}

	public void setPonavljanja(Integer ponavljanja) {
		this.ponavljanja = ponavljanja;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Trigram trigram = (Trigram)o;
		return Objects.equals(key, trigram.key);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key);
	}
}
