import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Cartella {
	final private int id;
	// TODO: 28/09/2019 cambiare struttura numeri, deve essere una matrice perchè devo sapere in che riga sono i numeri che segno
	private int[] numeri; // numeri contenuti in questa cartella
	final private boolean[] segnati; // true se il numero corrispondente è segnato
	private Vincita ultimaVincita;
	private LinkedList<Vincita> vincite;
	
	public Cartella(int[] num, int id) {
		segnati = new boolean[15]; //default tutti false
		numeri = num;
		this.id = id;
	}

	// Chiamata ogni volta che viene estratto un numero
	public boolean numeroEstratto(final int numero) {
		// cerca il numero tra quelli presenti nella cartella, e se presente lo segna estratto
		final int indice = Utility.indexOf(numero, numeri, Gestore.NUM_IN_UNA_CARTELLA);
		if (indice >= 0) {
			segnati[indice] = true;
			return true;
		}
		return false;
	}

	public void stampa(){
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

	public int[] getNumeri() {
		return numeri;
	}

	public boolean[] getSegnati() {
		return segnati;
	}

}
