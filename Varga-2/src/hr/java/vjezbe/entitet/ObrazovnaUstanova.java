package hr.java.vjezbe.entitet;

import java.util.List;

public abstract class ObrazovnaUstanova {

	private String NazivUstanove;
	private List<Predmet> Predmeti;
	private List<Profesor> Profesori;
	private List<Student> Studenati;
	private List<Ispit> Ispiti;

	public ObrazovnaUstanova(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, 
			List<Student> studenati, List<Ispit> ispiti) {
		NazivUstanove = nazivUstanove;
		Predmeti = predmeti;
		Profesori = profesori;
		Studenati = studenati;
		Ispiti = ispiti;
	}

	public String getNazivUstanove() {
		return NazivUstanove;
	}

	public void setNazivUstanove(String nazivUstanove) {
		NazivUstanove = nazivUstanove;
	}

	public List<Predmet> getPredmeti() {
		return Predmeti;
	}

	public void setPredmeti(List<Predmet> predmeti) {
		Predmeti = predmeti;
	}

	public List<Profesor> getProfesori() {
		return Profesori;
	}

	public void setProfesori(List<Profesor> profesori) {
		Profesori = profesori;
	}

	public List<Student> getStudenati() {
		return Studenati;
	}

	public void setStudenati(List<Student> studenati) {
		Studenati = studenati;
	}

	public List<Ispit> getIspiti() {
		return Ispiti;
	}

	public void setIspiti(List<Ispit> ispiti) {
		Ispiti = ispiti;
	}

	public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina);
}