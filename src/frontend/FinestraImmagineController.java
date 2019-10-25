package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class FinestraImmagineController {
	@FXML
	private AnchorPane pane;

	@FXML
	public ImageView immagine;

	@FXML
	public Label numero;

	@FXML
	public Label nomeImmagine;

	@FXML
	private void initialize(){
		// Queste cose servono per far ingrandire l'immagine man mano che si ingrandisce l'AnchorPane ovvero man mano
		// che si ingrandisce la finestra
		immagine.fitHeightProperty().bind(pane.heightProperty());
		immagine.fitWidthProperty().bind(pane.widthProperty());
	}

	public void mostraImmagine(File file){
		// Questi due valori servono per non far sgranare l'immagine, non so perchè però
		double altezza = pane.getMaxHeight();
		double larghezza = pane.getMaxWidth();
		System.out.println("Prima new image");
		Image image = new Image(file.toURI().toString(), altezza,larghezza,false,false);
		immagine.setImage(image);
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
}
