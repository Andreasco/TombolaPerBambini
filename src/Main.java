import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		GestoreCartelle gestoreCartelle = new GestoreCartelle();

		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci il numero di partecipanti");

		LinkedList<Cartella> cartelle = gestoreCartelle.caricaCartelle(sc.nextInt());

		Gestore gestore = new Gestore(cartelle);

		while (!gestore.tombola()) {
			System.out.println("Premi invio per andare avanti");
			sc.nextLine();

			gestore.estraiNumero();
		}
	}
	
}
