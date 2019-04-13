package hr.java.vjezbe.sortiranje;

import java.util.Comparator;

import hr.java.vjezbe.entitet.ObrazovnaUstanova;

public class BrojStudenataSorter implements Comparator<ObrazovnaUstanova> {
	@Override
	public int compare(ObrazovnaUstanova st1, ObrazovnaUstanova st2) {
		if (st1.getStudenati().size() > st2.getStudenati().size()) {
			return -1;
		} else if (st1.getStudenati().size() < st2.getStudenati().size()) {
			return 1;
		} else {
			if (st1.getNazivUstanove().compareTo(st2.getNazivUstanove()) == 1) {
				return -1;
			} else if (st1.getNazivUstanove().compareTo(st2.getNazivUstanove()) == -1) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
