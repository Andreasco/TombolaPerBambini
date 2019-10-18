package frontend;

import backend.Gestore;
import backend.Tabellone;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TabelloneController {
	private Gestore gestore;

	@FXML
	private GridPane tabellone;

	@FXML
	private GridPane grigliaVincite;

	@FXML
	private Label[] labelsVincite = new Label[5];

	@FXML
	private Label[] labelsNumeri = new Label[91]; //l'indice sarà il numero al suo interno

	@FXML
	private Label labelVinciteCartelle;

	@FXML
	private Label labelNotifiche;

	@FXML
	private Label labelNumeroEstratto;

	@FXML
	private Label labelNumeriUsciti;

	@FXML
	private Button bottoneMostraImmagine;

	@FXML
	private Button bottoneEstraiNumero;

	public TabelloneController() {
	}

	//funziona
	@FXML
	private void initialize(){
		//creo la lista delle label con i numeri
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

			bottoneMostraImmagine.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					//funziona mostra immagine backend
				}
			});

			bottoneEstraiNumero.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					//funziona estrai numero backend con conseguente cambio del colore della label
					//corrispondente al numero uscito
				}
			});
		}

		//creo la lista delle label con le vincite secondo i seguenti indici
		/*
		* Ambo = 0
		* Terna = 1
		* Quaterna = 2
		* Cinquina = 3
		* Tombola = 4
		 */
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



}
