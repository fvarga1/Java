package hr.java.vjezbe.entitet;

import java.time.LocalDate;

public class Student extends Osoba {

	private String jmbag;
	private LocalDate damtumRodjenja;

	public Student(String ime, String prezime, String jmbag, LocalDate damtumRodjenja) {
		super(ime, prezime);
		this.jmbag = jmbag;
		this.damtumRodjenja = damtumRodjenja;
	}

	public String getJmbag() {
		return jmbag;
	}

	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	public LocalDate getDamtumRodjenja() {
		return damtumRodjenja;
	}

	public void setDamtumRodjenja(LocalDate damtumRodjenja) {
		this.damtumRodjenja = damtumRodjenja;
	}
	
	

	@Override
	public String toString() {
		return this.getIme() + " " + this.getPrezime();
	}

	@Override
	public int hashCode() {
		// radim proizvoljno
		int prime = 31;
		int result = 1;
		result = prime * result + ((this.jmbag == null) ? 0 : this.jmbag.hashCode());
		result = prime * result + ((this.getIme() == null) ? 0 : this.getIme().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
        if (obj == null) return false;
        
        return ((this.jmbag).equals(((Student)obj).getJmbag()));
	}	
}
