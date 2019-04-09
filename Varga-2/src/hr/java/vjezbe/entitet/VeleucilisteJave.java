package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Logger;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {

	public VeleucilisteJave(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori,
			List<Student> studenati, List<Ispit> ispiti) {
		super(nazivUstanove, predmeti, profesori, studenati, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Ocjena ocjenaZavrsnog,
			Ocjena ocjenaObrane) {
		BigDecimal prosjekOcjena = null;
		try {
			prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispiti);
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			System.out.println(e.getMessage());
			// Ukoliko student ima nepolozene ispite, konacna ocjena studija je 1
			return BigDecimal.ONE;
		}
		return new BigDecimal(2).multiply(prosjekOcjena)
				.add(new BigDecimal(ocjenaZavrsnog.getVrijednost() + ocjenaObrane.getVrijednost()))
				.divide(new BigDecimal(4));
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		List<Ispit> ispitiSaGodine = this.getIspiti().stream().filter(x -> x.getDatumIVrijeme().getYear() == godina)
				.collect(Collectors.toList());
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati().get(0);
		BigDecimal najboljiProsjek = BigDecimal.ZERO;
		for (Student s : this.getStudenati()) {
			List<Ispit> ispitiStudenta = this.filtrirajIspitePoStudentu(ispitiSaGodine, s);
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
