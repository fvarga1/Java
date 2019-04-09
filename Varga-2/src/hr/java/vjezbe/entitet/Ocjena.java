package hr.java.vjezbe.entitet;

public enum Ocjena {

	NEDOVOLJAN(1, "Nedovoljan"), DOVLJAN(2, "Dovoljan"), DOBAR(3, "Dobar"), VRLO_DOBAR(4, "Vrlo dobar"),
	IZVRSTAN(5, "Izvrstan");

	private Integer vrijednost;
	private String opis;

	private Ocjena(Integer vrijednost, String opis) {
		this.vrijednost = vrijednost;
		this.opis = opis;
	}

	public Integer getVrijednost() {
		return vrijednost;
	}

	public String getOpis() {
		return opis;
	}
}
