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
	}

	// Chiamata ogni volta che viene estratto un numero
	public boolean numeroEstratto(final int numero) {
		// cerca il numero tra quelli presenti nella cartella, e se presente lo segna estratto
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 5; j++)
				if (numeri[i][j] == numero) {
					segnati[i][j] = true;
					return true;
				}
		return false;
	}

	private boolean controllaUltimaVincita(int riga, Vincita vincitaCorrente){
		if (ultimaVincita == null)
			return false;
		return riga == rigaUltimaVincita && vincitaCorrente.ordinal() - ultimaVincita.ordinal() == 1;
	}

	public boolean controllaVincita(Vincita vincitaCorrente){
		int countTombola = 0;
		int max = -1;
		int riga = -1;
		for (int i = 0; i < 3; i++) {
			int count = 0;
			for (Boolean e : segnati[i]) {
				if (e)
					count++;
			}
			if (count == 5) //riga tutta segnata
				countTombola++;

			if (count > max){
				max = count;
				riga = i;
			}
		}

		//vincitaCorrente.ordinal() - ultimaVincita.ordinal() == 1 vuol dire che la vincita corrente è successiva
		//all'ultima vincita di questa cartella quindi questa cartella non può vincere se il massimo(potenziale vincita)
		//si trova sulla stessa riga dell'ultima vincita
		if (max < 2 || controllaUltimaVincita(riga, vincitaCorrente) )
			return false;

		Vincita v;
		if (countTombola == 3) //tutte le righe segnate
			v = Vincita.Tombola;
		else {
			if (max == 2)
				v = Vincita.Ambo;
			else if (max == 3)
				v = Vincita.Terna;
			else if (max == 4)
				v = Vincita.Quaterna;
			else
				v = Vincita.Cinquina;
		}

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

	public int[][] getNumeri() {
		return numeri;
	}

	public boolean[][] getSegnati() {
		return segnati;
	}

}
