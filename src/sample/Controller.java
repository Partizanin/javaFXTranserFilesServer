package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.company.TCPSocketServer;

public class Controller {

    @FXML
    public TextArea messageArea;
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendButton;
    private TCPSocketServer server;

    public Controller() {
        messageArea = new TextArea();
        textArea = new TextArea();
        server = new TCPSocketServer(this);
    }

    void setStageAndSetupListeners(Stage primaryStage) {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                server.closeSocket();
            }
        });
      /*  primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.out.println("Stage is closing");
        });*/
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

    public void sendLog(String log) {
        textArea.appendText(log + "\n");
    }
}

