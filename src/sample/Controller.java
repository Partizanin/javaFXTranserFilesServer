package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import sample.company.Server;

public class Controller {

    private Server  server = new Server(this);

    @FXML
    public TextArea messageArea;
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendButton;

    public Controller() {
        messageArea = new TextArea();
        textArea = new TextArea();

        server.start();

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

