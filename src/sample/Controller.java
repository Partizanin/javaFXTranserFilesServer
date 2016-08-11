package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import sample.company.Client;

public class Controller {

    private Client client = new Client(this);
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendButton;


    public void setSendButton(){
        System.out.println("sendButton Action");
        client.sendFile();
    }

    public void sendMessage(String message) {
        textArea.appendText("\n" + message);
    }
}

