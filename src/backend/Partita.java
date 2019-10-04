package backend;

import java.util.LinkedList;

public class Partita {
	final private LinkedList<Cartella> cartelle;
	final private Tabellone tab;
	private Vincita vincitaCorrente;

	public Partita(LinkedList<Cartella> cartelle) {
		this.cartelle = cartelle;
		tab = new Tabellone();
		vincitaCorrente = Vincita.Ambo;
		Utility.info("Creata nuova partita");
	}

	public boolean estraiNumero() {
		final int n = tab.estraiNumero();
		Utility.info("Estratto numero: "+n);

		// controllo se il numero è presente in qualche cartella
		boolean vincita = false;
		for (Cartella c : cartelle) {
			if (c.numeroEstratto(n)) {
				Utility.info("La cartella numero "+c.getId()+" contiene il numero!");
				if (c.controllaVincita(vincitaCorrente)) {
					// il giocatore ha vinto
					vincita = true; //c'è stata una vincita quindi la vincita corrente deve cambiare
					Utility.info("La cartella numero " + c.getId() + " vince: " + vincitaCorrente.name());
				}
			}
		}

		if (vincita) {
			if (vincitaCorrente == Vincita.Tombola) {
				Utility.info("Tombola uscita, partita finita!");
				return true;
			}
			vincitaCorrente = Vincita.next(vincitaCorrente);
		}

		return false;
	}
}

