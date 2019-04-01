package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

	public FakultetRacunarstva(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenati,
			Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenati, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaDiplomskog,
			Integer ocjenaObrane) {
		BigDecimal prosjekOcjena = null;
		try {
			prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispiti);
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			System.out.println(e.getMessage());
			// Ukoliko student ima nepolozene ispite, konacna ocjena studija je 1
			return BigDecimal.ONE;
		}
		return  new BigDecimal(3)
				.multiply(prosjekOcjena)
				.add(new BigDecimal(ocjenaDiplomskog))
				.add(new BigDecimal(ocjenaObrane))
				.divide(new BigDecimal(5));
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() {
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		BigDecimal najboljiProsjek = BigDecimal.ZERO;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(this.getIspiti(), s);
			BigDecimal prosjekOcjena = BigDecimal.ZERO;
			try {
				prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispitiStudenta);
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				// Ukoliko student ima ispite ocjenjene negativnom ocjenom, preskacemo ga
				continue;
			}
			if (prosjekOcjena.compareTo(najboljiProsjek) == 1) {
				najboljiProsjek = prosjekOcjena;
				najUspjesnijiStudent = s;
			} else if (prosjekOcjena.compareTo(najboljiProsjek) == 0) {
				if (najUspjesnijiStudent.getDamtumRodjenja().isAfter(s.getDamtumRodjenja())) {
					najboljiProsjek = prosjekOcjena;
					najUspjesnijiStudent = s;
				}
			}
		}
		return najUspjesnijiStudent;
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ispitiSaGodine = (Ispit[]) Arrays.stream(this.getIspiti()).filter(x -> x.getDatumIVrijeme().getYear() == godina).toArray(Ispit[]::new);
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		int brojIzvrsnihTop = Integer.MIN_VALUE;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(ispitiSaGodine, s);
			int brojIzvrsnih = 0;
			for (Ispit ispit : ispitiStudenta) {
				if (ispit.getOcjena() == 5) {
					brojIzvrsnih++;
				}
			}
			if (brojIzvrsnih > brojIzvrsnihTop) {
				brojIzvrsnihTop = brojIzvrsnih;
				najUspjesnijiStudent = s;
			}
		}
		return najUspjesnijiStudent;
	}
}
