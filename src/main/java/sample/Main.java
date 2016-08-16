package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Сервер відпраки пошит");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        Controller controller = loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("MyGui.fxml"));
Parent root = (Parent)loader.load();
MyController controller = (MyController)loader.getController();
controller.setStageAndSetupListeners(stage);*/
    }
}
