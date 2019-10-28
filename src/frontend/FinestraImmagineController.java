package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

public class FinestraImmagineController {

	//L'anchor pane a sinistra nello Split Pane
	@FXML
	private AnchorPane paneSinistraSplitPane;

	//L'anchor pane che è grande quanto tutta la finestra
	@FXML
	private AnchorPane panePrincipale;

	@FXML
	private ImageView immagine;

	@FXML
	private Label numero;

	@FXML
	private Label nomeImmagine;

	@FXML
	private ImageView immagineIniziale;

	private String pathToDirectory;

	public FinestraImmagineController() {
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
	}

	@FXML
	private void initialize(){
		// Queste cose servono per far ingrandire l'immagine man mano che si ingrandisce l'AnchorPane ovvero man mano
		// che si ingrandisce la finestra
		immagine.fitHeightProperty().bind(paneSinistraSplitPane.heightProperty());
		immagine.fitWidthProperty().bind(paneSinistraSplitPane.widthProperty());
		immagineIniziale.fitHeightProperty().bind(panePrincipale.heightProperty());
		immagineIniziale.fitWidthProperty().bind(panePrincipale.widthProperty());

		File immagineIniziale = leggiImmagineIniziale();
		if (immagineIniziale != null) {
			mostraImmagineIniziale(immagineIniziale);
		}
	}

	public void mostraImmagine(File file){
		// Questi due valori servono per non far sgranare l'immagine, non so perchè però
		double altezza = paneSinistraSplitPane.getMaxHeight();
		double larghezza = paneSinistraSplitPane.getMaxWidth();
		System.out.println("Prima new image");
		Image image = new Image(file.toURI().toString(), altezza,larghezza,false,false);
		immagine.setImage(image);
	}

	public void mostraImmagineIniziale(File file) {
		// Questi due valori servono per non far sgranare l'immagine, non so perchè però
		double altezza = panePrincipale.getMaxHeight();
		double larghezza = panePrincipale.getMaxWidth();
		System.out.println("Prima new image (immagine iniziale)");
		Image image = new Image(file.toURI().toString(), altezza, larghezza, false, false);
		immagineIniziale.setImage(image);
	}

	public void mostraNomeImmagine(String nome){
		nomeImmagine.setText(nome);
	}

	public void setNumero(int n){
		numero.setText(n + "");
	}

	public void clearNumero(){
		numero.setText("");
	}

	public void nascondiImmagineIniziale() {
		immagineIniziale.setOpacity(0);
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
}
