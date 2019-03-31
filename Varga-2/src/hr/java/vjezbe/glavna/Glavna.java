package hr.java.vjezbe.glavna;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.VeleucilisteJave;

public class Glavna {

	/**
	 * Konstante za promjenu broja profesora, predmeta, studenata i isptinih rokova
	 */
	public static final int BROJ_PROFESORA = 2;
	public static final int BROJ_PREDMETA = 2;
	public static final int BROJ_STUDENATA = 2;
	public static final int BROJ_ISPITNIH_ROKOVA = 2;
	public static final int BROJ_OBRAZOVNIH_USTANOVA = 2;

	private static Scanner unos = new Scanner(System.in);
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");
	
	public static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	public static void main(String[] args) {
		
		logger.info("Program started");

		Profesor[] poljeProfesora = new Profesor[BROJ_PROFESORA];
		Predmet[] poljePredmeta = new Predmet[BROJ_PREDMETA];
		Student[] poljeStudenata = new Student[BROJ_STUDENATA];
		Ispit[] poljeIspita = new Ispit[BROJ_ISPITNIH_ROKOVA];
		ObrazovnaUstanova[] obrazovneUstanove = new ObrazovnaUstanova[BROJ_OBRAZOVNIH_USTANOVA];

		for (int j = 0; j < BROJ_OBRAZOVNIH_USTANOVA; j++) {
			System.out.println("Unesite podatke za " + (j + 1) + ". obrazovnu ustanovu");
			for (int i = 0; i < BROJ_PROFESORA; i++) {
				System.out.println("Unesite " + (i + 1) + ". profesora:");
				poljeProfesora[i] = unosProfesora(unos);
			}

			for (int i = 0; i < BROJ_PREDMETA; i++) {
				System.out.println("Unesite " + (i + 1) + ". predmet:");
				poljePredmeta[i] = unosPredmeta(unos, poljeProfesora);
			}

			for (int i = 0; i < BROJ_STUDENATA; i++) {
				System.out.println("Unesite " + (i + 1) + ". studenta:");
				poljeStudenata[i] = unosStudenta(unos);
			}

			for (int i = 0; i < BROJ_ISPITNIH_ROKOVA; i++) {
				System.out.println("Unesite " + (i + 1) + ". ispitni rok:");
				poljeIspita[i] = unosIspita(unos, poljePredmeta, poljeStudenata);
			}

			IspisiOdlicneStudente(poljeIspita);
			int odabir;
			do {
				System.out.println(
						"Odaberite obrazovnu ustanovu za navedene podatke koju želite unijeti (1 - Veleučilište Jave, 2 - Fakultet računarstva):");
				odabir = unos.nextInt();
				unos.nextLine();
			} while (odabir < 1 || odabir > 2);

			System.out.println("Unesite naziv ustanove:");
			String nazivUstanove = unos.nextLine();
			
			switch (odabir) {
			case 1:
				VeleucilisteJave vj = new VeleucilisteJave(nazivUstanove, poljePredmeta, poljeProfesora, poljeStudenata, poljeIspita);
				obrazovneUstanove[j] = vj;
				for (Student s :vj.getStudenati()) {
					System.out.println("Unesite ocjenu završnog rada za studenta " + s.getIme() + " " + s.getPrezime() + ":");
					int ocjenaZavrsnog = unos.nextInt();
					unos.nextLine();
					System.out.println("Unesite ocjenu obrane završnog rada za studenta " + s.getIme() + " " + s.getPrezime() + ":");
					int ocjenaObraneZavrsnog = unos.nextInt();
					unos.nextLine();
					System.out.println("Konacna ocjena studija studenta " + s.getIme() + " " + s.getPrezime() + ":" + vj.izracunajKonacnuOcjenuStudijaZaStudenta(poljeIspita, ocjenaZavrsnog, ocjenaObraneZavrsnog));
				}
				Student naj2018 = vj.odrediNajuspjesnijegStudentaNaGodini(2018);
				System.out.println("Najbolji student 2018. godine je " + naj2018.getIme() + " " + naj2018.getPrezime() + " JMBAG: " + naj2018.getJmbag());
				break;
			case 2:
				FakultetRacunarstva fr = new FakultetRacunarstva(nazivUstanove, poljePredmeta, poljeProfesora, poljeStudenata, poljeIspita);
				obrazovneUstanove[j] = fr;
				for (Student s : fr.getStudenati()) {
					System.out.println("Unesite ocjenu završnog rada za studenta " + s.getIme() + " " + s.getPrezime() + ":");
					int ocjenaZavrsnog = unos.nextInt();
					unos.nextLine();
					System.out.println("Unesite ocjenu obrane završnog rada za studenta " + s.getIme() + " " + s.getPrezime() + ":");
					int ocjenaObraneZavrsnog = unos.nextInt();
					unos.nextLine();
					System.out.println("Konacna ocjena studija studenta " + s.getIme() + " " + s.getPrezime() + ":" + fr.izracunajKonacnuOcjenuStudijaZaStudenta(poljeIspita, ocjenaZavrsnog, ocjenaObraneZavrsnog));
					
				}
				Student naj2018_2 = fr.odrediNajuspjesnijegStudentaNaGodini(2018);
				System.out.println("Najbolji student 2018. godine je " + naj2018_2.getIme() + " " + naj2018_2.getPrezime() + " JMBAG: " + naj2018_2.getJmbag());

				Student rektorovaNagrada = fr.odrediStudentaZaRektorovuNagradu();
				System.out.println("Student koji je osvojio rektorovu nagradu je " + rektorovaNagrada.getIme() + " " + rektorovaNagrada.getPrezime() + " JMBAG: " + rektorovaNagrada.getJmbag());
				break;
			default:
				break;
			}
		}
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

	public static Predmet unosPredmeta(Scanner unos, Profesor[] poljeProfesora) {

		System.out.println("Unesite širfu predmeta: ");
		String sifra = unos.nextLine();
		System.out.println("Unesite naziv predmeta: ");
		String naziv = unos.nextLine();
		System.out.println("Unesite broj ECTS bodova za predmet " + naziv + " ");
		Integer brojEctsBodova = unos.nextInt();
		unos.nextLine();

		System.out.println("Odaberite profesora: ");
		for (int i = 0; i < poljeProfesora.length; i++) {
			System.out.println((i + 1) + ". " + poljeProfesora[i].getIme() + " " + poljeProfesora[i].getPrezime());
		}

		System.out.print("Odabir >> ");
		Integer odabir = unos.nextInt();
		unos.nextLine();

		Predmet predmet = new Predmet(sifra, naziv, brojEctsBodova, poljeProfesora[odabir - 1]);

		System.out.println("Unesite broj studenata za predmet u " + naziv + " ");
		Integer brojstudenata = unos.nextInt();
		unos.nextLine();

		Student[] poljeStudenata = new Student[brojstudenata];

		predmet.setStudent(poljeStudenata);

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
		System.out.println("Unesite datum rođenja studenta " + ime + " " + prezime + " u formatu (dd.MM.yyyy.): ");
		LocalDate damtumRodjenja = LocalDate.parse(unos.next(), formatter);
		unos.nextLine();
		System.out.println("Unesite JMBAG studenta: " + ime + " " + prezime + ":");
		String jmbag = unos.nextLine();

		Student student = new Student(ime, prezime, jmbag, damtumRodjenja);

		return student;

	}

	/**
	 * Metoda za unos Ispita
	 */

	public static Ispit unosIspita(Scanner unos, Predmet[] poljePredmeta, Student[] poljeStudenata) {
		System.out.println("Odaberite predmet: ");
		for (int i = 0; i < poljePredmeta.length; i++) {
			System.out.println((i + 1) + ". " + poljePredmeta[i].getNaziv());
		}

		System.out.print("Odabir >> ");
		Integer odabirPredmeta = unos.nextInt();
		unos.nextLine();

		System.out.println("Odaberite studenta: ");
		for (int i = 0; i < poljeStudenata.length; i++) {
			System.out.println((i + 1) + ". " + poljeStudenata[i].getIme() + " " + poljeStudenata[i].getPrezime());
		}

		System.out.print("Odabir >> ");
		Integer odabirStudenta = unos.nextInt();
		unos.nextLine();

		System.out.print("Unesite ocjenu na ispitu (1-5):");
		Integer ocjena = unos.nextInt();
		unos.nextLine();
		System.out.println("Unesite datum i vrijeme ispita u formatu (dd.MM.yyyy.THH:mm): ");
		LocalDateTime vrijemeIspita = LocalDateTime.parse(unos.next(), formatter2);

		Ispit ispit = new Ispit(poljePredmeta[odabirPredmeta - 1], poljeStudenata[odabirStudenta - 1], ocjena,
				vrijemeIspita);

		return ispit;
	}

	private static void IspisiOdlicneStudente(Ispit[] ispitniRokovi) {
		for (Ispit ispit : ispitniRokovi) {
			Student s = ispit.getStudent();
			String np = ispit.getPredmet().getNaziv();
			if (ispit.getOcjena() == 5) {
				System.out.println("Student " + s.getIme() + " " + s.getPrezime() + " je ostvario ocjenu 'izvrstan' na predmetu '" + np + "'");
			}
		}

	}

}