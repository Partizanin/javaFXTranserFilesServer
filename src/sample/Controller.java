package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendButton;


    public void setSendButton(){
        System.out.println("sendButton Action");
        textArea.appendText("\n 11.11.2011 File found 1232133.txt sendButton Action");

    }
}

