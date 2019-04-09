package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

import ch.qos.logback.classic.Logger;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {

	public VeleucilisteJave(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenati,
			Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenati, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Ocjena ocjenaZavrsnog,
			Ocjena ocjenaObrane) {
		BigDecimal prosjekOcjena = null;
		try {
			prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispiti);
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			System.out.println(e.getMessage());
			// Ukoliko student ima nepolozene ispite, konacna ocjena studija je 1
			return BigDecimal.ONE;
		}
		return new BigDecimal(2)
				.multiply(prosjekOcjena)
				.add(new BigDecimal(ocjenaZavrsnog.getVrijednost() + ocjenaObrane.getVrijednost()))
				.divide(new BigDecimal(4));
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ispitiSaGodine = Arrays.stream(this.getIspiti()).filter(x -> x.getDatumIVrijeme().getYear() == godina)
				.toArray(Ispit[]::new);
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		BigDecimal najboljiProsjek = BigDecimal.ZERO;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(ispitiSaGodine, s);
			BigDecimal prosjekOcjena;
			try {
				prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispitiStudenta);
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				// Ukoliko student ima ispite ocjenjene negativnom ocjenom, preskacemo ga
				continue;
			}
			if (prosjekOcjena.compareTo(najboljiProsjek) == 1) {
				najboljiProsjek = prosjekOcjena;
				najUspjesnijiStudent = s;
			}
		}
		return najUspjesnijiStudent;
	}
}
