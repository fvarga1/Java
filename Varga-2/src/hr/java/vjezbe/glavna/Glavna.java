package hr.java.vjezbe.glavna;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Ocjena;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.Sveuciliste;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.sortiranje.BrojStudenataSorter;
import hr.java.vjezbe.sortiranje.StudentSorter;

public class Glavna {

	/**
	 * Konstante za promjenu broja profesora, predmeta, studenata i isptinih rokova
	 */
	public static final int BROJ_PROFESORA = 2;
	public static final int BROJ_PREDMETA = 3;
	public static final int BROJ_STUDENATA = 2;
	public static final int BROJ_ISPITNIH_ROKOVA = 2;
	public static final int BROJ_OBRAZOVNIH_USTANOVA = 2;

	private static boolean ispravanFormat = true;
	private static boolean ispravanUnos = true;

	private static Scanner unos = new Scanner(System.in);
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");

	public static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	public static void main(String[] args) {

		logger.info("Program started");

		List<Profesor> poljeProfesora = new ArrayList<>();
		List<Predmet> poljePredmeta = new ArrayList<>();
		List<Student> poljeStudenata = new ArrayList<>();
		List<Ispit> poljeIspita = new ArrayList<>();
		Sveuciliste<ObrazovnaUstanova> sveuciliste = new Sveuciliste<>();
		Map<Profesor, List<Predmet>> mapaProfesorPredmeti = new HashMap<>();

		for (int j = 0; j < BROJ_OBRAZOVNIH_USTANOVA; j++) {
			IsprazniKolekcije(poljeProfesora, poljePredmeta, poljeStudenata, poljeIspita, mapaProfesorPredmeti);
			System.out.println("Unesite podatke za " + (j + 1) + ". obrazovnu ustanovu");
			for (int i = 0; i < BROJ_PROFESORA; i++) {
				System.out.println("Unesite " + (i + 1) + ". profesora:");
				poljeProfesora.add(unosProfesora(unos));
			}

			for (int i = 0; i < BROJ_PREDMETA; i++) {
				System.out.println("Unesite " + (i + 1) + ". predmet:");
				poljePredmeta.add(unosPredmeta(unos, poljeProfesora, mapaProfesorPredmeti));
			}

			for (int i = 0; i < BROJ_STUDENATA; i++) {
				System.out.println("Unesite " + (i + 1) + ". studenta:");
				poljeStudenata.add(unosStudenta(unos));
			}

			for (int i = 0; i < BROJ_ISPITNIH_ROKOVA; i++) {
				System.out.println("Unesite " + (i + 1) + ". ispitni rok:");
				poljeIspita.add(unosIspita(unos, poljePredmeta, poljeStudenata));
			}

			IspisiOdlicneStudente(poljeIspita);
			IspisiStudentePoPredmetima(poljePredmeta);
			IspisiPredmetePoProfesorima(mapaProfesorPredmeti);

			int odabir = 0;
			// Dok god nije ispravan format vrtim do while petlju
			do {
				ispravanFormat = true;
				System.out.println(
						"Odaberite obrazovnu ustanovu za navedene podatke koju želite unijeti (1 - Veleučilište Jave, 2 - Fakultet računarstva):");
				try {
					odabir = unos.nextInt();
				} catch (InputMismatchException e) {
					System.out.println(
							"Unos ima neispravan format, ili neodgovarajuću vrijednost. Morat ćete unijet ponovo.");
					logger.error(e.getMessage());
					ispravanFormat = false;
				} finally {
					unos.nextLine();
				}
			} while (!ispravanFormat && (odabir < 1 || odabir > 2));

			System.out.println("Unesite naziv ustanove:");
			String nazivUstanove = unos.nextLine();

			switch (odabir) {
			case 1:
				sveuciliste.dodajObrazovnuUstanovu(new VeleucilisteJave(nazivUstanove, poljePredmeta, poljeProfesora,
						poljeStudenata, poljeIspita));
				VeleucilisteJave vj = (VeleucilisteJave) sveuciliste
						.dohvatiObrazovnuUstanovu(sveuciliste.getObrazovneUstanove().size() - 1);
				for (Student s : vj.getStudenati()) {
					try {
						List<Ispit> ispitiStudenta = filtrirajIspitePoStudentu(poljeIspita, s);
						vj.odrediProsjekOcjenaNaIspitima(poljeIspita);
					} catch (NemoguceOdreditiProsjekStudentaException e) {
						System.out.println(e.getMessage());
						// Ukoliko student ima nepolozene ispite, ne unosimo za njega ocjene zavrsnog
						continue;
					}
					int odabirOcjeneZavrsnog = 0;
					do {
						ispravanFormat = true;
						System.out.println("Odaberite ocjenu zavrsnog rada za studenta " + s.getIme() + " "
								+ s.getPrezime() + ":");
						int redniBroj = 1;
						for (Ocjena ocjena : Ocjena.values()) {
							System.out.println(redniBroj + ". " + ocjena.getOpis());
							redniBroj++;
						}
						try {
							odabirOcjeneZavrsnog = unos.nextInt();
						} catch (InputMismatchException e) {
							System.out.println(
									"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
							logger.error(e.getMessage());
							ispravanFormat = false;
						} finally {
							unos.nextLine();
						}
					} while (!ispravanFormat && (odabirOcjeneZavrsnog < 1 || odabirOcjeneZavrsnog > 5));
					Ocjena ocjenaZavrsnog = null;
					switch (odabirOcjeneZavrsnog) {
					case 2:
						ocjenaZavrsnog = Ocjena.DOVLJAN;
						break;
					case 3:
						ocjenaZavrsnog = Ocjena.DOBAR;
						break;
					case 4:
						ocjenaZavrsnog = Ocjena.VRLO_DOBAR;
						break;
					case 5:
						ocjenaZavrsnog = Ocjena.IZVRSTAN;
						break;
					default:
						ocjenaZavrsnog = Ocjena.NEDOVOLJAN;
						break;
					}
					int odabirOcjeneObraneZavrsnog = 0;
					do {
						ispravanFormat = true;
						System.out.println("Odaberite ocjenu obrane zavrsnog rada za studenta " + s.getIme() + " "
								+ s.getPrezime() + ":");
						int redniBroj = 1;
						for (Ocjena ocjena : Ocjena.values()) {
							System.out.println(redniBroj + ". " + ocjena.getOpis());
							redniBroj++;
						}
						try {
							odabirOcjeneObraneZavrsnog = unos.nextInt();
						} catch (InputMismatchException e) {
							System.out.println(
									"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
							logger.error(e.getMessage());
							ispravanFormat = false;
						} finally {
							unos.nextLine();
						}
					} while (!ispravanFormat && (odabirOcjeneObraneZavrsnog < 1 || odabirOcjeneObraneZavrsnog > 5));
					Ocjena ocjenaObraneZavrsnog = null;
					switch (odabirOcjeneObraneZavrsnog) {
					case 2:
						ocjenaObraneZavrsnog = Ocjena.DOVLJAN;
						break;
					case 3:
						ocjenaObraneZavrsnog = Ocjena.DOBAR;
						break;
					case 4:
						ocjenaObraneZavrsnog = Ocjena.VRLO_DOBAR;
						break;
					case 5:
						ocjenaObraneZavrsnog = Ocjena.IZVRSTAN;
						break;
					default:
						ocjenaObraneZavrsnog = Ocjena.NEDOVOLJAN;
						break;
					}
					System.out.println("Konacna ocjena studija studenta " + s.getIme() + " " + s.getPrezime() + ":"
							+ vj.izracunajKonacnuOcjenuStudijaZaStudenta(poljeIspita, ocjenaZavrsnog,
									ocjenaObraneZavrsnog));
				}
				Student naj2018 = vj.odrediNajuspjesnijegStudentaNaGodini(2018);
				System.out.println("Najbolji student 2018. godine je " + naj2018.getIme() + " " + naj2018.getPrezime()
						+ " JMBAG: " + naj2018.getJmbag());
				break;
			case 2:
				sveuciliste.dodajObrazovnuUstanovu(new FakultetRacunarstva(nazivUstanove, poljePredmeta, poljeProfesora,
						poljeStudenata, poljeIspita));
				FakultetRacunarstva fr = (FakultetRacunarstva) sveuciliste
						.dohvatiObrazovnuUstanovu(sveuciliste.getObrazovneUstanove().size() - 1);
				sveuciliste.dodajObrazovnuUstanovu(fr);
				for (Student s : fr.getStudenati()) {
					int odabirOcjeneZavrsnog = 0;
					do {
						ispravanFormat = true;
						System.out.println("Odaberite ocjenu zavrsnog rada za studenta " + s.getIme() + " "
								+ s.getPrezime() + ":");
						int redniBroj = 1;
						for (Ocjena ocjena : Ocjena.values()) {
							System.out.println(redniBroj + ". " + ocjena.getOpis());
							redniBroj++;
						}
						try {
							odabirOcjeneZavrsnog = unos.nextInt();
						} catch (InputMismatchException e) {
							System.out.println(
									"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
							logger.error(e.getMessage());
							ispravanFormat = false;
						} finally {
							unos.nextLine();
						}
					} while (!ispravanFormat && (odabirOcjeneZavrsnog < 1 || odabirOcjeneZavrsnog > 5));
					Ocjena ocjenaZavrsnog = null;
					switch (odabirOcjeneZavrsnog) {
					case 2:
						ocjenaZavrsnog = Ocjena.DOVLJAN;
						break;
					case 3:
						ocjenaZavrsnog = Ocjena.DOBAR;
						break;
					case 4:
						ocjenaZavrsnog = Ocjena.VRLO_DOBAR;
						break;
					case 5:
						ocjenaZavrsnog = Ocjena.IZVRSTAN;
						break;
					default:
						ocjenaZavrsnog = Ocjena.NEDOVOLJAN;
						break;
					}
					int odabirOcjeneObraneZavrsnog = 0;
					do {
						ispravanFormat = true;
						System.out.println("Odaberite ocjenu obrane zavrsnog rada za studenta " + s.getIme() + " "
								+ s.getPrezime() + ":");
						int redniBroj = 1;
						for (Ocjena ocjena : Ocjena.values()) {
							System.out.println(redniBroj + ". " + ocjena.getOpis());
							redniBroj++;
						}
						try {
							odabirOcjeneObraneZavrsnog = unos.nextInt();
						} catch (InputMismatchException e) {
							System.out.println(
									"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
							logger.error(e.getMessage());
							ispravanFormat = false;
						} finally {
							unos.nextLine();
						}
					} while (!ispravanFormat && (odabirOcjeneObraneZavrsnog < 1 || odabirOcjeneObraneZavrsnog > 5));
					Ocjena ocjenaObraneZavrsnog = null;
					switch (odabirOcjeneObraneZavrsnog) {
					case 2:
						ocjenaObraneZavrsnog = Ocjena.DOVLJAN;
						break;
					case 3:
						ocjenaObraneZavrsnog = Ocjena.DOBAR;
						break;
					case 4:
						ocjenaObraneZavrsnog = Ocjena.VRLO_DOBAR;
						break;
					case 5:
						ocjenaObraneZavrsnog = Ocjena.IZVRSTAN;
						break;
					default:
						ocjenaObraneZavrsnog = Ocjena.NEDOVOLJAN;
						break;
					}
					System.out.println("Konacna ocjena studija studenta " + s.getIme() + " " + s.getPrezime() + ":"
							+ fr.izracunajKonacnuOcjenuStudijaZaStudenta(poljeIspita, ocjenaZavrsnog,
									ocjenaObraneZavrsnog));

				}
				Student naj2018_2 = fr.odrediNajuspjesnijegStudentaNaGodini(2018);
				System.out.println("Najbolji student 2018. godine je " + naj2018_2.getIme() + " "
						+ naj2018_2.getPrezime() + " JMBAG: " + naj2018_2.getJmbag());

				Student rektorovaNagrada = null;
				try {
					rektorovaNagrada = fr.odrediStudentaZaRektorovuNagradu();
				} catch (PostojiViseNajmladjihStudenataException e) {
					logger.error(e.getMessage());
					System.out.println("Detektirano vise najmladjih studenata: " + e.getMessage());
					System.exit(0);
				}
				System.out.println("Student koji je osvojio rektorovu nagradu je " + rektorovaNagrada.getIme() + " "
						+ rektorovaNagrada.getPrezime() + " JMBAG: " + rektorovaNagrada.getJmbag());
				break;
			default:
				break;
			}
		}

		IspisiNajnapucenijeSveuciliste(sveuciliste);
	}

	private static List<Ispit> filtrirajIspitePoStudentu(List<Ispit> poljeIspita, Student s) {
		long start = System.currentTimeMillis();
		List<Ispit> ispiti = new ArrayList<>();
		for (Ispit ispit : poljeIspita) {
			if (ispit.getStudent().getJmbag() == s.getJmbag()) {
				ispiti.add(ispit);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("For filter : " + ((end - start) / 1000));

		long startLambda = System.currentTimeMillis();
		List<Ispit> ispitiLambda = ispiti.stream().filter(x -> x.getStudent().getJmbag() == s.getJmbag())
				.collect(Collectors.toList());
		long endLambda = System.currentTimeMillis();
		System.out.println("Lambda filter : " + ((endLambda - startLambda) / 1000));

		return ispitiLambda;
	}

	private static void IsprazniKolekcije(List<Profesor> poljeProfesora, List<Predmet> poljePredmeta,
			List<Student> poljeStudenata, List<Ispit> poljeIspita, Map<Profesor, List<Predmet>> mapaProfesorPredmeti) {

		poljeIspita.clear();
		poljePredmeta.clear();
		poljeProfesora.clear();
		poljeStudenata.clear();
		mapaProfesorPredmeti.clear();
	}

	/**
	 * Metoda za unos Profesora
	 * 
	 * @param unos
	 * @return
	 */

	public static Profesor unosProfesora(Scanner unos) {

		System.out.println("Unesite širfu profesora: ");
		String sifra = unos.nextLine();
		System.out.println("Unesite ime profesora: ");
		String ime = unos.nextLine();
		System.out.println("Unesite prezime profesora:: ");
		String prezime = unos.nextLine();
		System.out.println("Unesite titulu profesora: ");
		String titula = unos.nextLine();

		Profesor profesor = new Profesor(sifra, ime, prezime, titula);

		return profesor;

	}

	/**
	 * Metoda za unos Predmeta
	 * 
	 * @param unos
	 * @param poljeProfesora
	 * @return
	 */

	public static Predmet unosPredmeta(Scanner unos, List<Profesor> poljeProfesora,
			Map<Profesor, List<Predmet>> mapaProfesorPredmeti) {

		System.out.println("Unesite širfu predmeta: ");
		String sifra = unos.nextLine();
		System.out.println("Unesite naziv predmeta: ");
		String naziv = unos.nextLine();

//		 služi za primjer try catch-a
		/*
		 * do { ispravanFormat = true; System.out.println(
		 * "Odaberite obrazovnu ustanovu za navedene podatke koju želite unijeti (1 - Veleučilište Jave, 2 - Fakultet računarstva):"
		 * ); try { odabir = unos.nextInt(); } catch (InputMismatchException e) {
		 * System.out.println(
		 * "Unos ima neispravan format, ili neodgovarajuću vrijednost. Morat ćete unijet ponovo."
		 * ); logger.error(e.getMessage()); ispravanFormat = false; } finally {
		 * unos.nextLine(); } } while (!ispravanFormat && (odabir < 1 || odabir > 2));
		 */
		Integer brojEctsBodova = 0;
		do {
			ispravanUnos = true;
			System.out.println("Unesite broj ECTS bodova za predmet " + naziv + " ");
			try {
				brojEctsBodova = unos.nextInt();
			} catch (InputMismatchException e) {
				System.out
						.println("Unijeli ste neispravan broj ECTS bodova. Molimo unesite numerički broj ECTS bodova.");
				logger.error(e.getMessage());
				ispravanUnos = false;
			} finally {
				unos.nextLine();
			}
		} while (!ispravanUnos);

		Integer odabir = 0;
		do {
			ispravanUnos = true;
			System.out.println("Odaberite profesora: ");
			for (int i = 0; i < poljeProfesora.size(); i++) {
				System.out.println(
						(i + 1) + ". " + poljeProfesora.get(i).getIme() + " " + poljeProfesora.get(i).getPrezime());
			}
			try {
				System.out.print("Odabir >> ");
				odabir = unos.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Niste odabrali ispravan broj profesora. Molimo ponovite unos.");
				logger.error(e.getMessage());
				ispravanUnos = false;
			} finally {
				unos.nextLine();
			}
		} while (odabir < 1 || odabir > poljeProfesora.size());

		Predmet predmet = new Predmet(sifra, naziv, brojEctsBodova, poljeProfesora.get(odabir - 1));

		if (mapaProfesorPredmeti.containsKey(predmet.getNositelj())) {
			List<Predmet> ps = mapaProfesorPredmeti.get(predmet.getNositelj());
			ps.add(predmet);
			mapaProfesorPredmeti.put(predmet.getNositelj(), ps);
		} else {
			List<Predmet> ps = new ArrayList<>();
			ps.add(predmet);
			mapaProfesorPredmeti.put(predmet.getNositelj(), ps);
		}

//		System.out.println("Unesite broj studenata za predmet u " + naziv + " ");
//		Integer brojstudenata = unos.nextInt();
//		unos.nextLine();

		Set<Student> poljeStudenata = new TreeSet<>(new StudentSorter());
		predmet.setStudenti(poljeStudenata);

		return predmet;
	}

	/**
	 * Metoda za unos studenta
	 * 
	 * @param unos
	 * @return
	 */
	public static Student unosStudenta(Scanner unos) {

		System.out.println("Unesite ime studenta:");
		String ime = unos.nextLine();
		System.out.println("Unesite prezime studenta:");
		String prezime = unos.nextLine();
		System.out.println(
				"Unesite datum rođenja studenta " + ime + " " + prezime + " u formatu (dd.MM.yyyy.): 12.12.1994.");
		LocalDate damtumRodjenja = LocalDate.parse("12.12.1994.", formatter);
		unos.nextLine();
		System.out.println("Unesite JMBAG studenta: " + ime + " " + prezime + ":");
		String jmbag = unos.nextLine();

		Student student = new Student(ime, prezime, jmbag, damtumRodjenja);

		return student;

	}

	/**
	 * Metoda za unos Ispita
	 */

	public static Ispit unosIspita(Scanner unos, List<Predmet> poljePredmeta, List<Student> poljeStudenata) {
		Integer odabirPredmeta = 0;
		do {
			System.out.println("Odaberite predmet: ");
			for (int i = 0; i < poljePredmeta.size(); i++) {
				System.out.println((i + 1) + ". " + poljePredmeta.get(i).getNaziv());
			}
			System.out.print("Odabir >> ");
			odabirPredmeta = unos.nextInt();
			unos.nextLine();
		} while (odabirPredmeta < 1 || odabirPredmeta > poljePredmeta.size());

		Integer odabirStudenta = 0;
		do {
			System.out.println("Odaberite studenta: ");
			for (int i = 0; i < poljeStudenata.size(); i++) {
				System.out.println(
						(i + 1) + ". " + poljeStudenata.get(i).getIme() + " " + poljeStudenata.get(i).getPrezime());
			}
			System.out.print("Odabir >> ");
			odabirStudenta = unos.nextInt();
			unos.nextLine();
		} while (odabirStudenta < 0 || odabirStudenta > poljeStudenata.size());

		int odabirOcjeneNaIspitu = 0;
		do {
			ispravanFormat = true;
			System.out.println("Odaberite ocjenu studenta na ispitu:");
			int redniBroj = 1;
			for (Ocjena ocjena : Ocjena.values()) {
				System.out.println(redniBroj + ". " + ocjena.getOpis());
				redniBroj++;
			}
			try {
				odabirOcjeneNaIspitu = unos.nextInt();
			} catch (InputMismatchException e) {
				System.out.println(
						"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
				logger.error(e.getMessage());
				ispravanFormat = false;
			} finally {
				unos.nextLine();
			}
		} while (!ispravanFormat && (odabirOcjeneNaIspitu < 1 || odabirOcjeneNaIspitu > 5));
		Ocjena ocjenaNaIspitu = null;
		switch (odabirOcjeneNaIspitu) {
		case 2:
			ocjenaNaIspitu = Ocjena.DOVLJAN;
			break;
		case 3:
			ocjenaNaIspitu = Ocjena.DOBAR;
			break;
		case 4:
			ocjenaNaIspitu = Ocjena.VRLO_DOBAR;
			break;
		case 5:
			ocjenaNaIspitu = Ocjena.IZVRSTAN;
			break;
		default:
			ocjenaNaIspitu = Ocjena.NEDOVOLJAN;
			break;
		}
		LocalDateTime vrijemeIspita = null;
		do {
			ispravanFormat = true;
			try {
				System.out.println("Unesite datum i vrijeme ispita u formatu (dd.MM.yyyy.THH:mm): 12.12.2019.T10:00");
				vrijemeIspita = LocalDateTime.parse("12.12.2019.T10:00", formatter2);
			} catch (Exception e) {
				System.out.println(
						"Unos ima neispravan format, ili neodgovaraju�u vrijednost. Morat �ete unijet ponovo.");
				logger.error(e.getMessage());
				ispravanFormat = false;
			} finally {
				unos.nextLine();
			}
		} while (!ispravanFormat);

		Ispit ispit = new Ispit(poljePredmeta.get(odabirPredmeta - 1), poljeStudenata.get(odabirStudenta - 1),
				ocjenaNaIspitu, vrijemeIspita);

		return ispit;
	}

	private static void IspisiOdlicneStudente(List<Ispit> ispitniRokovi) {
		for (Ispit ispit : ispitniRokovi) {
			Student s = ispit.getStudent();
			String np = ispit.getPredmet().getNaziv();
			if (ispit.getOcjena().getVrijednost() == 5) {
				System.out.println("Student " + s.getIme() + " " + s.getPrezime()
						+ " je ostvario ocjenu 'izvrstan' na predmetu '" + np + "'");
			}
		}
	}

	private static void IspisiStudentePoPredmetima(List<Predmet> poljePredmeta) {
		long start = System.currentTimeMillis();
		for (Predmet predmet : poljePredmeta) {
			for (Student s : predmet.getStudenti()) {
				System.out.print("");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("For : " + ((end - start) / 1000));

		long startLambda = System.currentTimeMillis();
		poljePredmeta.forEach(x -> x.getStudenti().forEach(System.out::println));
		long endLambda = System.currentTimeMillis();
		System.out.println("For : " + ((endLambda - startLambda) / 1000));
	}

	private static void IspisiStudenteKojiSuDobiliOcjenuIzvrstan(List<Ispit> ispiti) {
		long start = System.currentTimeMillis();
		for (Ispit i : ispiti) {
			if (i.getOcjena().getVrijednost() == Ocjena.IZVRSTAN.getVrijednost()) {
				System.out.println(i.getStudent());
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("For : " + ((end - start) / 1000));

		long startLambda = System.currentTimeMillis();
		ispiti = ispiti.stream().filter(x -> x.getOcjena().getVrijednost() == Ocjena.IZVRSTAN.getVrijednost())
				.collect(Collectors.toList());
		ispiti.forEach(x -> System.out.println(x.getStudent()));
		long endLambda = System.currentTimeMillis();
		System.out.println("For : " + ((endLambda - startLambda) / 1000));
	}

	private static void IspisiPredmetePoProfesorima(Map<Profesor, List<Predmet>> mapaProfesorPredmeti) {
		// TODO IZGUGLAJ KAKO ISPISATI MAPU, I ONDA TI ISPISI PROFESORE I PREDMETE
		// KORISNO CE TI BITI DA DODAS TOSTRING METODE U PROFESOR I PREDMET AKO VEC NE
		// POSTOJE

		// Ne Moze
	}

	private static void IspisiNajnapucenijeSveuciliste(Sveuciliste<ObrazovnaUstanova> sveuciliste) {
		BrojStudenataSorter comp = new BrojStudenataSorter();
		ObrazovnaUstanova ou = sveuciliste.getObrazovneUstanove().stream().sorted((x, y) -> comp.compare(x, y))
				.collect(Collectors.toList()).get(0);
		System.out.println(ou.getNazivUstanove() + " ima najvise studenata");
	}
}
