import java.util.Arrays;

public class Cartella {
	final private int id;
	private int[] numeri; // numeri contenuti in questa cartella
	final private boolean[] segnati; // true se il numero corrispondente è segnato
	private Vincita vincita;
	
	public Cartella(int[] num, int id) {
		segnati = new boolean[15]; //default tutti false
		numeri = num;
		this.id = id;
	}

	public void stampa(){
		String risposta;
		if (vincita != null)
			risposta = " ha fatto " + vincita + "!!";
		else
			risposta = " purtroppo non ha vinto nulla :(";
		Utility.info("La cartella numero " + id + risposta);
	}

}
