import java.util.LinkedList;

public class Gestore {
	// Costanti intrinseche della tombola, NON modificabili
	public final static int    NUM_IN_UNA_CARTELLA    = 15;
	public final static int    RIGHE_IN_UNA_CARTELLA  = 3;

	private LinkedList<Cartella> cartelle;
	private boolean tombola;
	final private Partita p;
	
	public Gestore(LinkedList<Cartella> cartelle) {
		this.cartelle = cartelle;
		p = new Partita(cartelle);
	}

	public void estraiNumero(){
		if (p.estraiNumero()){
			tombola = true;

			// partita finita, riepilogo
			Utility.info("RIEPILOGO");
			for (Cartella c: cartelle) {
				c.stampa();
			}
		}
	}

	public boolean tombola(){
		return tombola;
	}
}

