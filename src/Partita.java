import java.util.ArrayList;
import java.util.LinkedList;

public class Partita {
	final private LinkedList<Cartella> cartelle;
	final private Tabellone tab;
	private Vincita ultimaVincita;

	public Partita(LinkedList<Cartella> cartelle) {
		this.cartelle = cartelle;
		tab = new Tabellone();
		ultimaVincita = null;
		Utility.info("Creata nuova partita");
	}

	public boolean estraiNumero() {
		final int n = tab.estraiNumero();
		Utility.info("Estratto numero: "+n);

		// controllo se il numero è presente in qualche cartella
		final ArrayList<Giocatore> vincitori = new ArrayList<>();
		for (Cartella c : cartelle) {
			if (c.numeroEstratto(n)) {
				Utility.info("La cartella numero "+c.getId()+" contiene il numero!");
				final Vincita v = c.vincitaMax();
				if (v == Vincita.next(ultimaVincita)) {
					// il giocatore ha vinto, aggiungi alla lista dei vincitori per dividere il premio
					vincitori.add(g);
					Utility.info("La cartella di "+g.getNome()+" vince: "+v.name());
					c.stampa();
				}
			}
		}

		// suddivide la vincita tra gli eventuali vincitori
		if (vincitori.size() > 0) {
			ultimaVincita = Vincita.next(ultimaVincita);
			float importo = calcolaVincita(ultimaVincita) / vincitori.size();
			Utility.info("Divido la vincita ("+ultimaVincita.name()+") tra "+vincitori.size()+" giocatori: ciascuno "+importo);
			for (Giocatore g: vincitori) {
				g.setVincita(ultimaVincita);
				g.aggiungiSommaVinta(importo);
			}
		}

		if (ultimaVincita == Vincita.Tombola) {
			Utility.info("Tombola uscita, partita finita!");
			return true;
		}

		return false;
	}
}

