package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;

public class Ispit {

	private Predmet predmet;
	private Student student;
	private Ocjena ocjena;
	private LocalDateTime datumIVrijeme;

	public Ispit(Predmet predmet, Student student, Ocjena ocjena, LocalDateTime datumIVrijeme) {
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
	}

	public Predmet getPredmet() {
		return predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Ocjena getOcjena() {
		return ocjena;
	}

	public void setOcjena(Ocjena ocjena) {
		this.ocjena = ocjena;
	}

	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}

	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}

	public String toString() {
		return "Student " + student.getIme() + " " + student.getPrezime() + " ocjena " + ocjena.getVrijednost()
				+ " naziv predmeta " + predmet.getNaziv();
	}
}
