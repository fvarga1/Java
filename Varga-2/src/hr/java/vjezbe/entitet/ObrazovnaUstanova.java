package hr.java.vjezbe.entitet;

public abstract class ObrazovnaUstanova {

	private String NazivUstanove;
	private Predmet[] Predmeti;
	private Profesor[] Profesori;
	private Student[] Studenati;
	private Ispit[] Ispiti;

	public ObrazovnaUstanova(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenati,
			Ispit[] ispiti) {
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

	public Predmet[] getPredmeti() {
		return Predmeti;
	}

	public void setPredmeti(Predmet[] predmeti) {
		Predmeti = predmeti;
	}

	public Profesor[] getProfesori() {
		return Profesori;
	}

	public void setProfesori(Profesor[] profesori) {
		Profesori = profesori;
	}

	public Student[] getStudenati() {
		return Studenati;
	}

	public void setStudenati(Student[] studenati) {
		Studenati = studenati;
	}

	public Ispit[] getIspiti() {
		return Ispiti;
	}

	public void setIspiti(Ispit[] ispiti) {
		Ispiti = ispiti;
	}

	public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina);
}