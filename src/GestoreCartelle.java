import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class GestoreCartelle {
	private LinkedList<Cartella> cartelle = new LinkedList<>(); //cartelle in memoria

	//carico in memoria le cartelle salvate sul file
	public GestoreCartelle(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("Cartelle"));
			for (int i = 0; i < 60; i++){
				String linea = br.readLine();
				StringTokenizer st = new StringTokenizer(linea, " ");
				int[] numeri = new int[15];
				for (int j = 0; j < 15; j++)
					numeri[j] = Integer.parseInt(st.nextToken());
				cartelle.add(new Cartella(numeri, i));
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
			res.add(cartelle.getFirst());
		return res;
	}
}
