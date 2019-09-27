public class Tabellone {

	private int[] numeri; // tutti i numeri da 1 a 90, in ordine casuale
	private int current; // elemento estratto da numeri

	final private boolean[] estratti; // true se l'elemento i-esimo è stato estratto
	
	public Tabellone() {
		estratti = new boolean[90]; // default tutti false

		// popola il vettore dei numeri e lo rimescola a caso (l'ordine di estrazione)
		numeri = new int[90];
		for (int i=0; i<90; i++)
			numeri[i] = i+1;
		Utility.shuffle(numeri);

		current=0;
	}

	public int estraiNumero() {
		int n = numeri[current];
		current++;
		estratti[n-1] = true;
		return n;
	}
	
	public void stampa() {
		for (int i=0; i<9; i++) {
			String output = "";
			for (int j=1; j<10; j++) {
				int n = i*10+j;
				boolean es = estratti[n];
				output += String.format("%c%02d%c", (es?'[':' '), n, (es?'[':' '));
			}
			Utility.info(output);
		}
	}

	
}
