package frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainFrontend extends Application {

	private Stage primaryStage;
	private Scene tabellone;
	private Stage stageImmagine;
	private Scene scenaImmagine;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Tabellone");

			// Serve per caricare la GUI descritta del file FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainFrontend.class.getResource("Tabellone.fxml"));
			tabellone = loader.load();
			// Carica il controller specificato alla riga 18 del file FXML, ovvero la class TabelloneController
			TabelloneController tabelloneController = loader.getController();

			//chiude tutta l'applicazione quando viene chiusa la finestra principale
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent windowEvent) {
					Platform.exit();
				}
			});

			primaryStage.setScene(tabellone);
			primaryStage.show();

			// Crea la finestra dove vengono mostrate l'immagine e il numero
			stageImmagine = new Stage();
			stageImmagine.setTitle("Immagine e numero");

			// Carica la GUI descritta nel file FXML
			loader = new FXMLLoader();
			loader.setLocation(MainFrontend.class.getResource("FinestraImmagine.fxml"));
			scenaImmagine = loader.load();

			tabelloneController.setFinestraImmagineController(loader.getController());
			tabelloneController.setPrimaryStage(primaryStage);

			stageImmagine.setScene(scenaImmagine);
			stageImmagine.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
