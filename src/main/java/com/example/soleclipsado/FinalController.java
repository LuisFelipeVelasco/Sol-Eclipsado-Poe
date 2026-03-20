package com.example.soleclipsado;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.text.Normalizer;
import java.util.*;

public class FinalController {

    @FXML
    private AnchorPane RootPane;
    @FXML
    private Label TextoFinal;
    @FXML
    protected void onHelloButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPalabra.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) RootPane.getScene().getWindow();
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }
    public void cambiarLabelPerdedor(){
        TextoFinal.setText("Intentos terminados,Perdiste");
    }
    public void cambiarLabelGanador(){
        TextoFinal.setText("¡¡GANASTE!!");
    }



}
