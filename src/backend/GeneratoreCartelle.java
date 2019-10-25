package backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class GeneratoreCartelle {
	public static void main(String[] args) {
		final int QUANTITA_CARTELLE = 60;
		final int NUMERI_PER_CARTELLA = 15;
		/*
		 * Questa classe serve solo per creare 60 cartelle casuali rispettando le regole della tombola.
		 * L'ho usata una sola volta e non dovrei aver bisogno di usarla di nuovo.
		 * Buona parte di questo codice è presa dal progetto trovato su GitHub quindi preferisco non metterci mano,
		 * basta che funzioni.
		 */


		for (int x = 0; x < QUANTITA_CARTELLE; x++) {
			int[] numeri;
			boolean[] segnati = new boolean[NUMERI_PER_CARTELLA];

			numeri = new int[NUMERI_PER_CARTELLA];
			// Riempio il vettore con 15 numeri casuali che rispettino le regole:
			// 1. no numeri ripetuti
			// 2. max 2 numeri con la stessa decina
			final int[] decine = new int[10]; //indica quanti numeri per ogni decina
			for (int i = 0; i < 15; i++) {
				// Genero un numero casuale tra 1 e 90
				final int n = generaCasuale(1, 90);
				// Decina attuale
				final int d = n == 90 ? 8 : n / 10; //il 90 va nella colonna degli 80

				// Se il numero casuale generato è già presente oppure se ci sono già due
				// numeri con la stessa decina, ripeto il calcolo dell'elemento i-esimo
				if (decine[d] >= 2 || indexOf(n, numeri, i) >= 0) {
					i--;
					continue;
				}
				else {
					numeri[i] = n;
					decine[d]++;
				}
			}

			// Ordina il vettore finale
			Arrays.sort(numeri);

			// Permuta per ottenere le righe finali (un elemento ogni tre nel vettore ordinato)
			int tmp = numeri[1];
			numeri[1] = numeri[3];
			numeri[3] = numeri[9];
			numeri[9] = numeri[13];
			numeri[13] = numeri[11];
			numeri[11] = numeri[5];
			numeri[5] = numeri[2];
			numeri[2] = numeri[6];
			numeri[6] = numeri[4];
			numeri[4] = numeri[12];
			numeri[12] = numeri[8];
			numeri[8] = numeri[10];
			numeri[10] = numeri[5];
			numeri[5] = tmp;

			// Scambia (in verticale) i numeri della stessa colonna se non sono in ordine tra loro
			for (int i = 0; i < 15; i++) {
				final int n = numeri[i];
				final int d = n / 10;
				for (int j = i; j < 15; j++) {
					final int n2 = numeri[j];
					final int d2 = n2 / 10;

					if (d == d2 && n > n2) { // d==d2: stessa colonna, n>n2 ordine invertito
						final int temp = numeri[i];
						numeri[i] = numeri[j];
						numeri[j] = temp;
					}
				}
			}

			BufferedWriter writer = null;
			try {
				//create a temporary file
				File logFile = new File("src/persistenza/Cartelle");

				// This will output the full path where the file will be written to...
				System.out.println(logFile.getCanonicalPath());

				writer = new BufferedWriter(new FileWriter(logFile, true));

				final String spacer = " ";
				for (int r = 0; r < 3; r++) {
					String output = "";
					int d = 0;
					for (int c = 0; c < 5; c++) {
						int index = r * 5 + c;
						int num = numeri[index];

						// spazi per i numeri mancanti (per incolonnare i numeri nella giusta decina)
						int _d = (int) ((double) num / 10.0);
						if (num == 90) // il 90 va nella colonna degli 80
							_d = 8;
						for (int i = 1; i < _d - d + (c == 0 ? 1 : 0); i++)
							//output += spacer + " ";
							d = _d;

						// stampa numero
						output += String.format("%s%02d%c", "", num, (segnati[index] ? '#' : ' '));
					}
					if (r == 2)
						output = output.substring(0,output.length()-1);
					writer.write(output);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					// Close the writer regardless of what happens...
					if(x != 59)
						writer.write("\n");
					writer.close();
				}
				catch (Exception e) {
				}
			}
		}
	}

	private static int generaCasuale(final int min, final int max) {
		return min + (int)(Math.random() * (max-min));
	}

	// Controlla se un numero è presente in un array e ne restituisce l'indice, o -1 se non presente
	private static int indexOf(int numero, int[] array, int size) {
		for (int i = 0; i < size; i++) {
			if (numero == array[i])
				return i;
		}
		return -1;
	}
}
