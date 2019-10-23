package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class GestoreCartelle {

	/**
	 * Questa classe serve per caricare in memoria le cartelle salvate sul file di testo.
	 * Per ogni riga del file di testo crea una matrice 3x5 che rappresenta i numeri della cartella,
	 * crea la cartella passandogli la matrice e salva la cartella in una Linked List che verrà poi scorsa
	 * per ricavare le N cartelle necessarie.
	 */

	private LinkedList<Cartella> cartelle = new LinkedList<>(); //cartelle in memoria

	//carico in memoria le cartelle salvate sul file
	public GestoreCartelle(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/persistenza/Cartelle"));
			for (int i = 0; i < 60; i++){
				String linea = br.readLine();
				StringTokenizer st = new StringTokenizer(linea, " ");
				int [][] numeri = new int[3][5];
				for (int k = 0; k < 3; k++) {
					int[] riga = new int[5];
					for (int j = 0; j < 5; j++)
						riga[j] = Integer.parseInt(st.nextToken());
					numeri[k] = riga;
				}
				cartelle.add(new Cartella(numeri, i+1));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//restituisco una lista delle prime n cartelle, in base al numero dei partecipanti
	public LinkedList<Cartella> caricaCartelle(int quantita){
		LinkedList<Cartella> res = new LinkedList<>();
		for (int i = 0; i < quantita; i++)
			res.add(cartelle.removeFirst());
		return res;
	}
}
