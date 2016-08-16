package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.company.Server;
import sample.company.TCPSocketServer;

public class Controller {

    private TCPSocketServer server ;

    @FXML
    public TextArea messageArea;
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendButton;

    void setStageAndSetupListeners(Stage primaryStage) {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                server.closeSocket();
                System.out.println(server.getSocket() + " Stage is closing");
            }
        });
      /*  primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.out.println("Stage is closing");
        });*/
    }


    public Controller() {
        messageArea = new TextArea();
        textArea = new TextArea();
        final Controller controller = this;

        Thread thread = new Thread(){
            @Override
            public void run() {
                server = new TCPSocketServer(controller);
                server.getFilesFromStation();
            }
        };

        thread.start();
    }

    private void changeColor(String color) {
        if (color.equals("red")) {
            messageArea.setStyle("-fx-text-fill: red ;");
        } else {
            messageArea.setStyle("-fx-text-fill:  chartreuse ;");
        }
    }

    public void sendButtonAction() {
        System.out.println("Send button");
    }

    public void sendMessage(String message, String color) {
        changeColor(color);
        messageArea.appendText(message + "\n");
    }

    public  void sendLog(String log) {
        textArea.appendText(log + "\n");
    }
}

