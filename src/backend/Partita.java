package backend;

import frontend.TabelloneController;

import java.util.LinkedList;

public class Partita {

	/**
	 * Rappresenta la partita vera e propria, estrae effettivamente il numero, controlla le vincite di ogni cartella
	 * e tiene traccia delle vincite uscite durante la partita.
	 */

	//Se impostato a true stampa sulla GUI, se impostato a false stampa su System.out
	private static boolean GUI = true;

	final private LinkedList<Cartella> cartelle;
	final private Tabellone tab;
	private Vincita vincitaCorrente;

	//Mi serve per effettuare le stampe sui componenti del controller
	private TabelloneController tabelloneController;

	public Partita(LinkedList<Cartella> cartelle, TabelloneController tabelloneController) {
		this.cartelle = cartelle;
		tab = new Tabellone();
		vincitaCorrente = Vincita.Ambo;
		this.tabelloneController = tabelloneController;
		if (GUI)
			tabelloneController.printlnNotifica("Creata nuova partita");
		else
			Utility.info("Creata nuova partita");
	}

	public int estraiNumero() {
		final int n = tab.estraiNumero();
		if (GUI) {
			tabelloneController.printlnNumeroEstratto(n + "");
			tabelloneController.printlnNumeriUsciti(n + "");
		}
		else
			Utility.info("Estratto numero: "+n);
		return n;
	}

	public boolean controllaVincite(int n){
		// ritorna true se è uscita la tombola
		// controllo se il numero è presente in qualche cartella, eventualmente lo segno e controllo se ha vinto qualcosa
		boolean vincita = false;
		for (Cartella c : cartelle) {
			// il metodo che effettivamente controlla se la cartella contiene il numero e se ha vinto qualcosa
			// e restituisce la riga dove si trova il numero appena segnato
			int riga = c.numeroEstratto(n);
			if (riga != -1) {
				if (GUI)
					tabelloneController.printlnNotifica("La cartella numero " + c.getId() + " contiene il numero " + n);
				else
					Utility.info("La cartella numero "+c.getId()+" contiene il numero " + n);
				if (c.controllaVincita(vincitaCorrente, riga)) {
					// il giocatore ha vinto
					vincita = true; //c'è stata una vincita quindi la vincita corrente deve cambiare
					if (GUI) {
						tabelloneController.printlnVinciteCartelle("La cartella numero " + c.getId() + " vince: " + vincitaCorrente.name());
						tabelloneController.segnaVincita(vincitaCorrente);
					}
					else
						Utility.info("La cartella numero " + c.getId() + " vince: " + vincitaCorrente.name());
				}
			}
		}

		if (vincita) {
			if (vincitaCorrente == Vincita.Tombola) {
				if (GUI)
					tabelloneController.printlnNotifica("Tombola uscita, partita finita!");
				else
					Utility.info("Tombola uscita, partita finita!");
				return true;
			}
			vincitaCorrente = Vincita.next(vincitaCorrente);
		}

		return false;
	}
}

