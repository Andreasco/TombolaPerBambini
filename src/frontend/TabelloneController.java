package frontend;

import backend.Cartella;
import backend.Gestore;
import backend.GestoreCartelle;
import backend.Vincita;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TabelloneController {
	/**
	 * Gestisce tutta la finestra del tabellone e anche qualcosa in più (lo so, non dovrebbe)
	 * È davvero un casino
	 */

	@FXML
	private GridPane tabellone;

	@FXML
	private GridPane grigliaVincite;

	public Label[] labelsVincite = new Label[5];

	public Label[] labelsNumeri = new Label[91]; //l'indice sarà il numero al suo interno

	@FXML
	private TextArea areaVinciteCartelle;

	@FXML
	private TextArea areaNotifiche;

	@FXML
	private Label labelNumeroEstratto;

	@FXML
	private TextArea areaNumeriUsciti;

	@FXML
	private Button bottoneMostraImmagine;

	private String nomeImmagineAttuale;

	@FXML
	private Button bottoneMostraNome;

	@FXML
	private Button bottoneEstraiNumero;

	@FXML
	private MenuItem impostaNumeroPartecipanti;

	@FXML
	private MenuItem caricaImmagini;

	private Gestore gestore;

	private LinkedList<Cartella> cartelle;

	private TabelloneController tabelloneController;

	private FinestraImmagineController finestraImmagineController;

	private Stage primaryStage;

	private int numeroImmaginiCaricate;

	// Mi salvo gli indici (riga dove si trova il path) delle immagini usate così non carico due volte la stessa
	private LinkedList<Integer> indiciRigaImmaginiUsate = new LinkedList<>();

	public TabelloneController() {
		tabelloneController = this;
	}

	@FXML
	private void initialize(){
		bottoneEstraiNumero.setDisable(true);
		bottoneMostraImmagine.setDisable(true);
		bottoneMostraNome.setDisable(true);

		//creo la lista delle label con i numeri
		creaLabelsNumeri();

		//creo la lista delle label con le vincite secondo i seguenti indici
		/*
		* Ambo = 0
		* Terna = 1
		* Quaterna = 2
		* Cinquina = 3
		* Tombola = 4
		 */
		creaLabelsVincite();

		aggiungiListeners();
	}

	// Dato che è una struttura ad albero io prendo la griglia principale e scendo tra i rami fino ad arrivare alle label
	private void creaLabelsNumeri() {
		for (Node sezione : tabellone.getChildren()){
			if (sezione instanceof GridPane){
				GridPane gridPane = (GridPane) sezione;
				for (Node cella : gridPane.getChildren()){
					if (cella instanceof AnchorPane){
						AnchorPane anchorPane = (AnchorPane) cella;
						Node numero = anchorPane.getChildren().get(0);
						if (numero instanceof Label) {
							Label label = (Label) numero;
							int indice = Integer.parseInt(label.getText());
							labelsNumeri[indice] = label;
						}
					}
				}
			}
		}
	}

	private void creaLabelsVincite() {
		for (Node cella : grigliaVincite.getChildren()){
			if (cella instanceof AnchorPane){
				AnchorPane anchorPane = (AnchorPane) cella;
				Node numero = anchorPane.getChildren().get(0);
				if (numero instanceof Label) {
					Label label = (Label) numero;
					String testo = label.getText();
					switch (testo) {
						case "Ambo":
							labelsVincite[0] = label;
							break;
						case "Terna":
							labelsVincite[1] = label;
							break;
						case "Quaterna":
							labelsVincite[2] = label;
							break;
						case "Cinquina":
							labelsVincite[3] = label;
							break;
						case "Tombola":
							labelsVincite[4] = label;
							break;
					}
				}
			}
		}
	}

	// Aggiungo i listener ad ongi pulsante o item dei menù
	private void aggiungiListeners() {
		bottoneEstraiNumero.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent actionEvent) {
				int n = gestore.estraiNumero();
				labelsNumeri[n].setStyle("-fx-background-color: #1dff31");
				finestraImmagineController.setNumero(n);

				// Se esce la tombola
				if (gestore.controllaVincite()) {
					areaNotifiche.appendText("È uscita la tombola!" + "\n");
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "È uscita la tombola!");
					alert.showAndWait();
					bottoneEstraiNumero.setDisable(true);
					bottoneMostraImmagine.setDisable(true);
					bottoneMostraNome.setDisable(true);
				}
			}
		});

		impostaNumeroPartecipanti.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

				// Creo la finestra da mostrare che chiede il numero dei partecipanti
				Label labelNumeroPartecipanti = new Label("Imposta numero partecipanti:");
				TextField fieldNumeroPartecipanti = new TextField();
				Button bottoneNumeroPartecipanti = new Button("OK");

				// Per mettere i componenti in ordine uno sopra l'altro
				VBox secondaryLayout = new VBox();
				secondaryLayout.getChildren().add(labelNumeroPartecipanti);
				secondaryLayout.getChildren().add(fieldNumeroPartecipanti);
				secondaryLayout.getChildren().add(bottoneNumeroPartecipanti);

				Scene secondScene = new Scene(secondaryLayout, 230, 100);

				// New window (Stage)
				Stage newWindow = new Stage();
				newWindow.setTitle("Imposta numero partecipanti");
				newWindow.setScene(secondScene);

				newWindow.initOwner(primaryStage);
				// Serve per impedire all'utente di interagire con la finestra principale se non chiude prima questa
				newWindow.initModality(Modality.WINDOW_MODAL);

				// Il bottone OK di questa finestra
				bottoneNumeroPartecipanti.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						// Se non ha inserito nessun numero lo avviso e gli permetto di reinserirlo
						if (fieldNumeroPartecipanti.getText().equals("") || fieldNumeroPartecipanti.getText() == null){
							Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai inserito nessun numero");
							alert.showAndWait();
							return;
						}

						int numeroCartelle = Integer.parseInt(fieldNumeroPartecipanti.getText());
						GestoreCartelle gestoreCartelle = new GestoreCartelle();
						cartelle = gestoreCartelle.caricaCartelle(numeroCartelle);
						gestore = new Gestore(cartelle, tabelloneController);
						bottoneEstraiNumero.setDisable(false);
						bottoneMostraImmagine.setDisable(false);
						bottoneMostraNome.setDisable(false);
						newWindow.close();
					}
				});

				newWindow.show();
			}
		});

		caricaImmagini.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				List<File> immagini = fileChooser.showOpenMultipleDialog(primaryStage);

				// TODO: 24/10/2019 il numero delle immagini ovviamente non viene salvato tra una partita e l'altra,
				// cambiare il metodo che salva i path in modo da salvare il numero delle immagini alla prima riga
				// cosicchè all'avvio di una partita basti leggere la prima riga per sapere quante immagini ci sono.
				// Se non ci sono immagini in memoria avviso l'utente
				if (immagini != null && !immagini.isEmpty()) {
					numeroImmaginiCaricate = immagini.size();
					salvaPathImmagini(immagini);
				}

				else{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai selezionato nessuna immagine");
					alert.showAndWait();
				}
			}
		});

		bottoneMostraImmagine.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//NON DOVREBBE MAI SUCCEDERE perchè la partita dovrebbe finire prima ma serve
				//per evitare che il programma vada in blocco continuando a cercare un indice che non è stato usato
				if (indiciRigaImmaginiUsate.size() == numeroImmaginiCaricate)
					return;
				System.out.println("Prima scegli file casuale");
				File file = scegliImmagineCasuale();
				nomeImmagineAttuale = file.getName();
				finestraImmagineController.mostraNomeImmagine("");
				finestraImmagineController.clearNumero();
				finestraImmagineController.mostraImmagine(file);
			}
		});

		bottoneMostraNome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				finestraImmagineController.mostraNomeImmagine(nomeImmagineAttuale);
			}
		});
	}

	private void salvaPathImmagini(List<File> files){
		try {
			FileWriter fw = new FileWriter("src/persistenza/ListaImmagini");

			for (File immagine : files) {
				fw.write(immagine.getAbsolutePath() + "\n");
			}

			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//sceglie un'immagine casuale ma mai due volte la stessa
	private File scegliImmagineCasuale(){
		File ret = null;
		int indice;
		do {
			System.out.println("Prima random");
			indice = new Random().nextInt(numeroImmaginiCaricate);
			System.out.println(indice);
		} while (indiciRigaImmaginiUsate.contains(indice));

		indiciRigaImmaginiUsate.add(indice);

		try {
			System.out.println("Prima FIS");
			FileInputStream fs= new FileInputStream("src/persistenza/ListaImmagini");
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			for(int i = 0; i < indice; ++i)
				br.readLine();
			String pathImmagine = br.readLine();
			ret = new File(pathImmagine);
			System.out.println("Dopo FIS");
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/* STAMPE */

	public void printlnNotifica(String s) {
		areaNotifiche.appendText(s + "\n");
		areaNotifiche.setScrollTop(Double.MAX_VALUE);
	}

	public void printlnVinciteCartelle(String s) {
		areaVinciteCartelle.appendText(s + "\n");
		areaVinciteCartelle.setScrollTop(Double.MAX_VALUE);
	}

	public void printlnNumeroEstratto(String s) {
		labelNumeroEstratto.setText(s);
	}

	public void printlnNumeriUsciti(String s) {
		areaNumeriUsciti.appendText(" " + s);
	}

	public void segnaVincita(Vincita vincita) {
		labelsVincite[vincita.ordinal()].setStyle("-fx-background-color: #1dff31");
	}

	/* SETTERS */

	public void setFinestraImmagineController(FinestraImmagineController finestraImmagineController) {
		this.finestraImmagineController = finestraImmagineController;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
