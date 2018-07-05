package me.vukas;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class TrigramKey implements Serializable {
	private String r1;
	private String r2;
	private String r3;

	//needed for serialization
	public TrigramKey(){}

	public TrigramKey(String r1, String r2, String r3) {
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}

	public String getR1() {
		return r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TrigramKey that = (TrigramKey)o;
		return Objects.equals(r1, that.r1) &&
				Objects.equals(r2, that.r2) &&
				Objects.equals(r3, that.r3);
	}

	@Override
	public int hashCode() {

		return Objects.hash(r1, r2, r3);
	}
}
