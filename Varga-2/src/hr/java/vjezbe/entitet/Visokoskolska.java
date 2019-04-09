package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

public interface Visokoskolska {

	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Ocjena ocjenaZavrsnog,
			Ocjena ocjenaObrane);

	public default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispiti) throws NemoguceOdreditiProsjekStudentaException {
		int zbrojPozitivnihOcjena = 0;
		int brojOcjena = 0;
		for (Ispit ispit : filtrirajPolozeneIspite(ispiti)) {
			zbrojPozitivnihOcjena += ispit.getOcjena().getVrijednost();
			brojOcjena++;
		}
		float prosjek = (float) zbrojPozitivnihOcjena / brojOcjena;
		return new BigDecimal(prosjek);
	}
	
	public default List<Ispit> filtrirajIspitePoStudentu(List<Ispit> ispiti, Student student) {
		return ispiti.stream().filter(
				x -> x.getStudent().getJmbag().equals(student.getJmbag()))
				.collect(Collectors.toList());
	}

	private List<Ispit> filtrirajPolozeneIspite(List<Ispit> ispiti) throws NemoguceOdreditiProsjekStudentaException {
		List<Ispit> nepolozeniIspiti = ispiti.stream().filter(
				x -> x.getOcjena().getVrijednost() == 1).collect(Collectors.toList());
		if (nepolozeniIspiti.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Ispit ispit : nepolozeniIspiti) {
				stringBuilder.append(ispit.getPredmet().getNaziv() + " (1); ");
			}
			throw new NemoguceOdreditiProsjekStudentaException(stringBuilder.toString());
		}
		return ispiti.stream().filter(x -> x.getOcjena().getVrijednost() > 1)
				.collect(Collectors.toList());
	}
}
