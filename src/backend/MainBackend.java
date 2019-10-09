package backend;

import java.util.LinkedList;
import java.util.Scanner;

public class MainBackend {

	public static void main(String[] args) {
		GestoreCartelle gestoreCartelle = new GestoreCartelle();

		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci il numero di partecipanti");

		int n = Integer.parseInt(sc.nextLine());
		LinkedList<Cartella> cartelle = gestoreCartelle.caricaCartelle(n);

		Gestore gestore = new Gestore(cartelle);

		while (!gestore.tombola()) {
			System.out.println("Premi invio per andare avanti");
			sc.nextLine();

			gestore.estraiNumero();
		}
	}
	
}
