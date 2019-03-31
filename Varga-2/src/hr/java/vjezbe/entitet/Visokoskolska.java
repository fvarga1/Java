package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

public interface Visokoskolska {

	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaZavrsnog,
			Integer ocjenaObrane);

	public default BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) {
		int zbrojPozitivnihOcjena = 0;
		int brojOcjena = 0;
		for (Ispit ispit : filtrirajPolozeneIspite(ispiti)) {
			zbrojPozitivnihOcjena += ispit.getOcjena();
			brojOcjena++;
		}
		float prosjek = (float) zbrojPozitivnihOcjena / brojOcjena;
		return new BigDecimal(prosjek);
	}
	
	public default Ispit[] filtrirajIspitePoStudentu(Ispit[] ispiti, Student student) {
		return Arrays.stream(ispiti).filter(x -> x.getStudent().getJmbag().equals(student.getJmbag())).toArray(Ispit[]::new);
	}

	private Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti) {
		return Arrays.stream(ispiti).filter(x -> x.getOcjena() > 1).toArray(Ispit[]::new);
	}
}
