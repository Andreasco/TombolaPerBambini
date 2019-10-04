package backend;

import java.util.ArrayList;

public class Giocatore {
	private final String nome;
	private Cartella cart;
	private ArrayList<Vincita> vincite;
	private float sommaVinta;

	public Giocatore (String nome_) {
		nome = nome_;
		vincite = new ArrayList<>();
		sommaVinta = 0;
	}
	
	public void nuovaCartella() { // da chiamare ad ogni inizio di partita
		//cart = new Cartella();
		Utility.info("Nuova cartella per "+getNome());
		cart.stampaVincite();
	}
	public Cartella getCartella() {
		return cart;
	}
	
	public String getNome() {
		return nome;
	}

	
	public void aggiungiSommaVinta(float importo) {
		if (importo > 0)
			sommaVinta += importo;
	}
	public float getSommaVinta() {
		return sommaVinta;
	}
	
	public boolean getVincita(Vincita v) {
		return vincite.contains(v);
	}
	public void setVincita(Vincita v) {
		vincite.add(v);
	}
	
	public void stampa() {
		Utility.info("Giocatore: "+getNome());
		
		String vv = "Vincite: ";
		for (Vincita v: Vincita.values()) {
			if (getVincita(v))
				vv += v.name()+" ";
		}
		Utility.info(vv);
		Utility.info("Somma vinta: "+getSommaVinta());
		Utility.info();

	}

}
