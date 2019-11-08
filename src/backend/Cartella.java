package backend;

import java.util.Iterator;
import java.util.LinkedList;

public class Cartella {
	final private int id;
	private int[][] numeri; // numeri contenuti in questa cartella
	private boolean[][] segnati; // true se il numero corrispondente è segnato
	private Vincita ultimaVincita;
	private int rigaUltimaVincita;
	private LinkedList<Vincita> vincite = new LinkedList<>();
	
	public Cartella(int[][] num, int id) {
		segnati = new boolean[3][5]; //default tutti false
		numeri = num;
		this.id = id;
		rigaUltimaVincita = -1;
	}

	// Chiamata ogni volta che viene estratto un numero
	public int numeroEstratto(final int numero) {
		// cerca il numero tra quelli presenti nella cartella, e se presente lo segna estratto
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 5; j++)
				if (numeri[i][j] == numero) {
					segnati[i][j] = true;
					return i;
				}
		return -1;
	}

	public boolean controllaVincita(Vincita vincitaCorrente, int riga){
		if (vincitaCorrente == Vincita.Tombola)
			return controllaTombola();
		else
			return controllaRiga(vincitaCorrente, riga);
	}

	// Controlla se è uscita la tombola
	private boolean controllaTombola(){
		for (boolean[] riga : segnati)
			for (boolean elemento : riga)
				if (!elemento)
					return false;
		return true;
	}

	// Gli viene passata la riga su cui è uscito il numero e controlla se su quella riga c'è stata una vincita
	private boolean controllaRiga(Vincita vincitaCorrente, int riga){
		if (riga == rigaUltimaVincita && vincitaCorrente.ordinal() - ultimaVincita.ordinal() == 1)
			return false;
		int count = 0;
		int i = 0;
		for (boolean elemento : segnati[riga]) {
			if (elemento) {
				count++;
				System.out.println("Cartella: " + id + " riga: " + riga + " numero: " + numeri[riga][i]);
			}
			i++;
		}
		Vincita v = null;
		if (count == 2)
			v = Vincita.Ambo;
		else if (count == 3)
			v = Vincita.Terna;
		else if (count == 4)
			v = Vincita.Quaterna;
		else if (count == 5)
			v = Vincita.Cinquina;

		if (v == vincitaCorrente) {
			vincite.add(v);
			rigaUltimaVincita = riga;
			ultimaVincita = v;
			return true;
		}
		else
			return false;
	}

	public void stampaVincite(){
		String risposta;
		if (vincite.size() != 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(" ha fatto: ");
			Iterator<Vincita> it = vincite.iterator();
			while (it.hasNext()){
				sb.append(it.next());
				if (it.hasNext())
					sb.append(", ");
			}

			risposta = sb.toString();
		}
		else
			risposta = " purtroppo non ha vinto nulla :(";
		Utility.info("La cartella numero " + id + risposta);
	}

	public int getId() {
		return id;
	}

}
