package hr.java.vjezbe.entitet;

public class Profesor extends Osoba {

	private String sifra;
	private String titula;

	public Profesor(String sifra, String ime, String prezime, String titula) {
		super(ime, prezime);
		this.sifra = sifra;
		this.titula = titula;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}

}
