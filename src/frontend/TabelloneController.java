package frontend;

import backend.Cartella;
import backend.Gestore;
import backend.GestoreCartelle;
import backend.Vincita;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TabelloneController {
	/**
	 * Gestisce tutta la finestra del tabellone e anche qualcosa in più (lo so, non dovrebbe)
	 * È davvero un casino
	 */

	private Label[] labelsVincite = new Label[5];

	private Label[] labelsNumeri = new Label[91]; //l'indice corrisponde al numero all'interno della label

	@FXML
	private GridPane tabellone;

	@FXML
	private GridPane grigliaVincite;

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

	@FXML
	private Button bottoneMostraNome;

	@FXML
	private Button bottoneEstraiNumero;

	@FXML
	private MenuItem impostaNumeroPartecipanti;

	@FXML
	private MenuItem caricaImmagini;

	@FXML
	private MenuItem apriFinestraImmagine;

	@FXML
	private MenuItem caricaImmagineIniziale;

	@FXML
	private MenuItem mostraImmagineIniziale;

	@FXML
	private MenuItem about;

	@FXML
	private MenuItem comeFunziona;

	private TabelloneController tabelloneController;

	private MainFrontend mainFrontend;

	private FinestraImmagineController finestraImmagineController;

	private Stage primaryStage;

	private Gestore gestore;

	private LinkedList<Cartella> cartelle;

	private int numeroImmaginiCaricate = 0;

	private String nomeImmagineAttuale;

	private File immagineCorrente;

	private int numeroCorrente;

	private boolean premutoMostraImmagine = false;

	private boolean premutoMostraNome = false;

	private boolean premutoEstraiNumero = false;

	// Path della directory in cui si trova il JAR
	private String pathToDirectory;

	// Mi salvo gli indici (riga dove si trova il path) delle immagini usate così non carico due volte la stessa
	private LinkedList<Integer> indiciRigaImmaginiUsate = new LinkedList<>();

	public TabelloneController() {
		try {
			String pathToJar = new File(TabelloneController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			String[] pathSeparato = pathToJar.split("/");

			// Path fino alla directory in cui si trova la tombola
			pathToDirectory = "";
			for (int i = 0; i < pathSeparato.length - 1; i++)
				pathToDirectory += "/" + pathSeparato[i];
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Mi serve perchè devo passarlo al Gestore
		tabelloneController = this;

		// Leggo quante immagini sono salvate, se ce ne sono
		leggiQuantitaImmagini();
	}

	@FXML
	private void initialize(){
		bottoneEstraiNumero.setDisable(true);
		if (numeroImmaginiCaricate == 0) {
			bottoneMostraImmagine.setDisable(true);
			bottoneMostraNome.setDisable(true);
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non ci sono immagini caricate");
			alert.showAndWait();
		}

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

		aggiungiListenerImpostaNumeroPartecipanti();

		aggiungiListenerCaricaImmagini();

		aggiungiListenerApriFinestraImmagine();

		aggiungiListenerCaricaImmagineIniziale();

		aggiungiListenerMostraImmagineIniziale();

		aggiungiListenerComeFunziona();

		aggiungiListenerAbout();

		aggiungiListenerBottoneMostraImmagine();

		aggiungiListenerBottoneMostraNome();

		aggiungiListenerBottoneEstraiNumero();

	}

	/* LISTENERS */

	private void aggiungiListenerBottoneEstraiNumero() {
		bottoneEstraiNumero.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent actionEvent) {
				int n = gestore.estraiNumero();
				labelsNumeri[n].setStyle("-fx-background-color: #1dff31");
				numeroCorrente = n;
				finestraImmagineController.setNumero(n);
				premutoEstraiNumero = true;

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
	}

	private void aggiungiListenerImpostaNumeroPartecipanti() {
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
						if (numeroCartelle > 60) {
							Alert alert = new Alert(Alert.AlertType.INFORMATION, "Troppi giocatori, purtroppo questo " + "programma supporta fino a 60 giocatori");
							alert.showAndWait();
							return;
						}
						GestoreCartelle gestoreCartelle = new GestoreCartelle();
						cartelle = gestoreCartelle.caricaCartelle(numeroCartelle);
						gestore = new Gestore(cartelle, tabelloneController);
						bottoneEstraiNumero.setDisable(false);
						finestraImmagineController.nascondiImmagineIniziale();
						newWindow.close();
					}
				});

				mainFrontend.setPrimaVolta(false);
				newWindow.show();
			}
		});
	}

	private void aggiungiListenerCaricaImmagini() {
		caricaImmagini.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				List<File> immagini = fileChooser.showOpenMultipleDialog(primaryStage);

				if (immagini != null && !immagini.isEmpty()) {
					numeroImmaginiCaricate = immagini.size();
					salvaPathImmagini(immagini);
					bottoneMostraImmagine.setDisable(false);
					bottoneMostraNome.setDisable(false);
				}

				else{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai selezionato nessuna immagine");
					alert.showAndWait();
				}
			}
		});
	}

	private void aggiungiListenerBottoneMostraImmagine() {
		bottoneMostraImmagine.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//NON DOVREBBE MAI SUCCEDERE perchè la partita dovrebbe finire prima ma serve
				//per evitare che il programma vada in blocco continuando a cercare un indice che non è stato usato
				if (indiciRigaImmaginiUsate.size() == numeroImmaginiCaricate)
					return;

				premutoMostraNome = false;
				premutoEstraiNumero = false;

				System.out.println("Prima scegli file casuale");
				File file = scegliImmagineCasuale();
				nomeImmagineAttuale = file.getName().split("\\.")[0];
				immagineCorrente = file;
				finestraImmagineController.mostraNomeImmagine("");
				finestraImmagineController.clearNumero();
				finestraImmagineController.mostraImmagine(file);
				premutoMostraImmagine = true;
			}
		});
	}

	private void aggiungiListenerBottoneMostraNome() {
		bottoneMostraNome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				finestraImmagineController.mostraNomeImmagine(nomeImmagineAttuale);
				premutoMostraNome = true;
			}
		});
	}

	private void aggiungiListenerComeFunziona() {
		comeFunziona.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try {
					Stage stageComeFunziona = new Stage();
					stageComeFunziona.setTitle("Come funziona");

					// Carica la GUI descritta nel file FXML
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainFrontend.class.getResource("ComeFunziona.fxml"));
					Scene scenaComeFunziona = loader.load();

					stageComeFunziona.setScene(scenaComeFunziona);
					stageComeFunziona.show();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void aggiungiListenerAbout() {
		about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try {
					Stage stageAbout = new Stage();
					stageAbout.setTitle("About");

					// Carica la GUI descritta nel file FXML
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainFrontend.class.getResource("About.fxml"));
					Scene scenaAbout = loader.load();

					stageAbout.setScene(scenaAbout);
					stageAbout.show();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void aggiungiListenerApriFinestraImmagine() {
		apriFinestraImmagine.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mainFrontend.apriFinestraImmagine();
				if (premutoMostraImmagine)
					finestraImmagineController.mostraImmagine(immagineCorrente);
				if (premutoMostraNome)
					finestraImmagineController.mostraNomeImmagine(nomeImmagineAttuale);
				if (premutoEstraiNumero)
					finestraImmagineController.setNumero(numeroCorrente);
			}
		});
	}

	private void aggiungiListenerCaricaImmagineIniziale() {
		caricaImmagineIniziale.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File immagine = fileChooser.showOpenDialog(primaryStage);

				if (immagine != null) {
					salvaPathImmagineIniziale(immagine);
				}

				else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai selezionato nessuna immagine");
					alert.showAndWait();
				}
			}
		});
	}

	private void aggiungiListenerMostraImmagineIniziale() {
		mostraImmagineIniziale.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				finestraImmagineController.mostraImmagineIniziale();
			}
		});
	}

	/* UTILITA */

	// Legge la prima riga del file con la lista delle immagini per vedere quante immagini ci sono salvate
	private void leggiQuantitaImmagini() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToDirectory + "/ListaImmagini"));
			numeroImmaginiCaricate = Integer.parseInt(br.readLine());
			br.close();
		}
		catch (Exception e) {
			numeroImmaginiCaricate = 0;
		}
	}

	private File leggiImmagineIniziale() {
		File ret = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToDirectory + "/ImmagineIniziale"));
			String pathImmagine = br.readLine();
			System.out.println(pathImmagine);
			ret = new File(pathImmagine);

			br.close();
		}
		catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai selezionato nessuna immagine iniziale");
			alert.showAndWait();
		}

		return ret;
	}

	private void salvaPathImmagini(List<File> files){
		try {
			FileWriter fw = new FileWriter(pathToDirectory + "/ListaImmagini");

			// Scrive al primo rigo quante immagini ci sono
			fw.write(files.size() + "\n");

			for (File immagine : files) {
				fw.write(immagine.getAbsolutePath() + "\n");
			}

			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void salvaPathImmagineIniziale(File immagineIniziale) {
		try {
			FileWriter fw = new FileWriter(pathToDirectory + "/ImmagineIniziale");

			// Scrive al primo rigo il path dell'immagine iniziale
			fw.write(immagineIniziale.getAbsolutePath() + "\n");

			fw.close();
		}
		catch (Exception e) {
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
			BufferedReader br = new BufferedReader(new FileReader(pathToDirectory + "/ListaImmagini"));
			for (int i = 0; i < indice + 1; ++i)
				br.readLine();
			String pathImmagine = br.readLine();
			ret = new File(pathImmagine);
			System.out.println("Dopo FIS");
			br.close();
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

	void setFinestraImmagineController(FinestraImmagineController finestraImmagineController) {
		this.finestraImmagineController = finestraImmagineController;
	}

	void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	void setMainFrontend(MainFrontend mainFrontend) {
		this.mainFrontend = mainFrontend;
	}
}
