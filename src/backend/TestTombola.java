package backend;

import java.util.LinkedList;

public class TestTombola {
	/**
	 * Classe usata per fare test vari, potrebbe servirmi
	 */

	public static void main(String[] args) {
		int[] numeri = new int[]{4,21, 30, 55, 63, 8, 28, 40, 59, 84, 15, 35, 44, 65, 89};
		GestoreCartelle gc = new GestoreCartelle();
		LinkedList<Cartella> cartelle = gc.caricaCartelle(1);
		Cartella c = cartelle.getFirst();
		Vincita v = Vincita.Ambo;
		Utility.shuffle(numeri);
		for (Integer i : numeri){
			int riga = c.numeroEstratto(i);
			if (riga != -1) {
				if (c.controllaVincita(v, riga))
					v = Vincita.next(v);
			}
		}
	}
}
