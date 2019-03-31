package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {

	public VeleucilisteJave(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenati,
			Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenati, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaZavrsnog,
			Integer ocjenaObrane) {
		return new BigDecimal(2)
				.multiply(this.odrediProsjekOcjenaNaIspitima(ispiti))
				.add(new BigDecimal(ocjenaZavrsnog + ocjenaObrane))
				.divide(new BigDecimal(4));
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ispitiSaGodine = Arrays.stream(this.getIspiti()).filter(x -> x.getDatumIVrijeme().getYear() == godina).toArray(Ispit[]::new);
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		BigDecimal najboljiProsjek = BigDecimal.ZERO;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(ispitiSaGodine, s);
			BigDecimal prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispitiStudenta);
			if (prosjekOcjena.compareTo(najboljiProsjek) == 1) {
				najboljiProsjek = prosjekOcjena;
				najUspjesnijiStudent = s;
			}
		}
		return najUspjesnijiStudent;
	}
}
