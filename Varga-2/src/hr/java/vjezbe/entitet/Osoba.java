package hr.java.vjezbe.entitet;

public abstract class Osoba {

	private String Ime;
	private String Prezime;

	public Osoba(String ime, String prezime) {
		this.Ime = ime;
		this.Prezime = prezime;
	}

	public String getIme() {
		return Ime;
	}

	public void setIme(String ime) {
		this.Ime = ime;
	}

	public String getPrezime() {
		return Prezime;
	}

	public void setPrezime(String prezime) {
		this.Prezime = prezime;
	}
}
