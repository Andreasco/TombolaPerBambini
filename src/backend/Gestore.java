package backend;

import frontend.TabelloneController;

import java.util.LinkedList;

public class Gestore {
	/**
	 * Questa classe serve per gestire la partita dal frontend, banalmente serve per estrarre il numero e
	 * dire alla partita di controllare se ci sono state vincite.
	 */

	final private Partita p;
	private int numeroEstratto;
	
	public Gestore(LinkedList<Cartella> cartelle, TabelloneController tabelloneController) {
		p = new Partita(cartelle, tabelloneController);
	}

	public int estraiNumero(){
		numeroEstratto = p.estraiNumero();
		return numeroEstratto;
	}

	//ritorna true se è uscita la tombola
	public boolean controllaVincite(){
		return p.controllaVincite(numeroEstratto);
	}
}

