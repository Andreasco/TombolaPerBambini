package backend;

import java.util.Random;

public class Utility {

	// Se INFO_OUTPUT è false, tutte le classi diventeranno "silenziose": tutto l'output
	// delle classi passa attraverso il metodo Utility.info, quindi basta porre INFO_OUTPUT
	// a false per eliminare tutto l'output
	public static boolean INFO_OUTPUT = true;


	public static int generaCasuale(final int min, final int max) {
		return min + (int)(Math.random() * (max-min));
	}

	// Controlla se un numero è presente in un array e ne restituisce l'indice, o -1 se non presente
	public static int indexOf(int numero, int[] array, int size) {
		for (int i = 0; i < size; i++) {
			if (numero == array[i])
				return i;
		}
		return -1;
	}

	// Stampa informazioni a schermo
	public static void info(final String s) {
		if (INFO_OUTPUT) {
			String[] lines = s.split(System.getProperty("line.separator"));
			for (String l: lines)
				System.out.println(">>> "+l);
		}
	}
	// Variante senza argomenti, stampa solo una riga vuota
	public static void info() {
		if (INFO_OUTPUT)
			System.out.println();
	}

	// Rimescola un array in ordine casuale
	public static void shuffle(final int[] array) {
		int index, temp;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
	}

}
