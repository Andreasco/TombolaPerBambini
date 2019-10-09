package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFrontend extends Application {

	private Stage primaryStage;
	private Scene tabellone;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Tabellone");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainFrontend.class.getResource("Tabellone.fxml"));
			tabellone = loader.load();
			TabelloneController tabelloneController = loader.getController(); //non so a cosa serva

			primaryStage.setScene(tabellone);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
