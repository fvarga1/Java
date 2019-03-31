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

}