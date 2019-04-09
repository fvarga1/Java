package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

	public FakultetRacunarstva(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenati,
			Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenati, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Ocjena ocjenaDiplomskog,
			Ocjena ocjenaObrane) {
		BigDecimal prosjekOcjena = null;
		try {
			prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispiti);
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			System.out.println("Student " + ispiti[0].getStudent().getIme() + " " + ispiti[0].getStudent().getPrezime() + 
					" zbog negativne ocjene na jednom od ispita ima prosjek „nedovoljan (1)!");
			Glavna.logger.error(e.getMessage());
			// Ukoliko student ima nepolozene ispite, konacna ocjena studija je 1
			return BigDecimal.ONE;
		}
		return new BigDecimal(3)
				.multiply(prosjekOcjena)
				.add(new BigDecimal(ocjenaDiplomskog.getVrijednost()))
				.add(new BigDecimal(ocjenaObrane.getVrijednost()))
				.divide(new BigDecimal(5));
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() throws PostojiViseNajmladjihStudenataException {
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		BigDecimal najboljiProsjek = BigDecimal.ZERO;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(this.getIspiti(), s);
			BigDecimal prosjekOcjena = BigDecimal.ZERO;
			try {
				prosjekOcjena = this.odrediProsjekOcjenaNaIspitima(ispitiStudenta);
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				Glavna.logger.error(e.getMessage());
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
				} else if (najUspjesnijiStudent.getDamtumRodjenja().isEqual(s.getDamtumRodjenja())) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("Najmlaði studenti istih prosjeka i datuma rodjenja: ");
					stringBuilder.append(najUspjesnijiStudent.getIme() + " " + najUspjesnijiStudent.getPrezime());
					stringBuilder.append(" i " + s.getIme() + " " + s.getPrezime());
					throw new PostojiViseNajmladjihStudenataException(stringBuilder.toString());
				} 
			}
		}
		return najUspjesnijiStudent;
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ispitiSaGodine = (Ispit[]) Arrays.stream(this.getIspiti()).filter(x -> x.getDatumIVrijeme().getYear() == godina)
				.toArray(Ispit[]::new);
		// Postavljam defaultno da je prvi student u arrayu najuspjesniji
		Student najUspjesnijiStudent = this.getStudenati()[0];
		int brojIzvrsnihTop = Integer.MIN_VALUE;
		for (Student s : this.getStudenati()) {
			Ispit[] ispitiStudenta = this.filtrirajIspitePoStudentu(ispitiSaGodine, s);
			int brojIzvrsnih = 0;
			for (Ispit ispit : ispitiStudenta) {
				if (ispit.getOcjena().getVrijednost() == 5) {
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
