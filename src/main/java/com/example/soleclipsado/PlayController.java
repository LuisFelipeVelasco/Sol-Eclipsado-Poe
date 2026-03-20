package com.example.soleclipsado;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

/*
@author Luis Felipe Velasco Chilito 2517245-3743
@version 1.0.4

 */
public class PlayController {
    @FXML
    private HBox hbox;
    @FXML
    private AnchorPane AnchorRoot;
    @FXML
    private ImageView imageSol;

    @FXML
    private Label AdvertenciaText;
    public String PalabraSecreta;
    private List<TextField> textFields = new ArrayList<>();
    int ContadorPistasDadas=0;
    boolean valorDeIntento;
    int ContadorEstadoImagenSolEclipsado=1;
    int ContadorLetrasAcertadas=0;
    int NumeroMaximoPistasDadas=3;


    @FXML
    //Metodo que se activa cada vex que el boton pista es precionado y verifica si dar o no una pista
    protected void onActionButtonClicked(){
        if (ContadorPistasDadas<NumeroMaximoPistasDadas){ //Debe ser menor para no dar mas pistas que el numero maximo
            ContadorPistasDadas+=1;
            for(TextField textField : textFields){//Recorre toda la lista de textfields
                if(textField.getText().isEmpty()){//encuentra el primer textfield vacio
                    PistaAgregarLetra(textField);//llama al Metodo PistaAgregarLetra pasandole como parametro dicho textfield
                    break;//detiene el bucle
                }
            }
            //Si usando una pista se completa la palabra, entonces mostrar la pantalla de victoria
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else DiseñoLabelText(AdvertenciaText,"No puedes usar mas pistas","red" );
    }

    //Metodo para crear los campos de texto de acuerdo a la longitud de la palabra
    protected void CrearCamposdeTextoEnInterfaz(){

        Integer GuardarLongitudPalabraSecreta= PalabraSecreta.length();
        for(Integer i=0; i< GuardarLongitudPalabraSecreta; i++){
            TextField textField = GetTextField(); //Instancia un campo de texto con estilo
            textFields.add(textField); //Añade al objeto texfield en la lista de textFields
            hbox.getChildren().add(textField); //Agrega el campo de texto en el hbox
        }
    }
    //Metodo que retorna un textField con un estilo
    protected TextField GetTextField() {

        TextField textField= new TextField(); //Instancia un nuevo texfield
        textField.setStyle( "-fx-font-size: 15; " + "-fx-font-family: 'Arial'; " + "-fx-font-weight: bold; " + //Estilo para textfield
                        "-fx-alignment: center; "  + "-fx-border-color: #34495e; " + "-fx-border-width: 1; " + "-fx-border-radius: 2;" +"-fx-min-height: 40; " +
                "-fx-max-height: 40; " +
                "-fx-pref-height: 40; "
        );
        return textField;
    }

    //Metodo que le asigna a cada texfield el event listener setOnKeyTyped y hace que ControladorCampoDeTexto se encargue del evento
    protected void AsignarSetOnKeyTypedACamposDeTexto(){

        for (TextField textField : textFields){
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                ControladorCampoDeTexto(textField, newValue);});
        }
    }
    //Metodo que controla/verifica las LetraIngresadaUsuarios de los campos de texto
    protected void ControladorCampoDeTexto(TextField textField,String LetraIngresadaUsuario){
        if (LetraIngresadaUsuario.isEmpty()) return;

        /* * replaceAll("\\p{M}", "") y toLowerCase(Locale.ROOT) limpian y estandarizan el texto:
         * \\p{M} -> Busca "Marks" (Marcas combinadas), que son los acentos/diéresis sueltos que dejó el NFD.
         * "" -> Reemplaza esos acentos por nada (los elimina por completo).
         * toLowerCase() -> Convierte toda la palabra a minúsculas.
         * Locale.ROOT -> Asegura que la conversión a minúsculas sea universal y no dependa del idioma del sistema.
         */
        LetraIngresadaUsuario=Normalizer.normalize(LetraIngresadaUsuario, Normalizer.Form.NFD);
        LetraIngresadaUsuario = LetraIngresadaUsuario.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        System.out.println(LetraIngresadaUsuario);
        if(IngresoDeSoloLetrasEnCampoDeTexto(textField, LetraIngresadaUsuario)){
            valorDeIntento=VerificarLetraIngresadaUsuarioCoincideEnPalabraSecreta(textField,LetraIngresadaUsuario);//variable que guarda si el usuario se equivoco o no
            CambiarSolEclipsado();
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Metodo que Evita que el usuario ingrese un caracter especial o un numero a un campo de texto
    protected boolean IngresoDeSoloLetrasEnCampoDeTexto(TextField textField,String LetraIngresadaUsuario){

        if (!LetraIngresadaUsuario.matches("\\p{L}+") && !LetraIngresadaUsuario.isEmpty()) {
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("Ojo..  nada de numeros o caracteres especiales");
            textField.setText("");
            return false;
        }
        return true;
    }

    //Metodo que verifica que la LetraIngresadaUsuario en un campo de texto coincida con la letra de la palabra secreta en esa posicion
    protected  boolean VerificarLetraIngresadaUsuarioCoincideEnPalabraSecreta(TextField textField,String LetraIngresadaUsuario){

        String LetraCorrecta= LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        if(Objects.equals(LetraCorrecta, LetraIngresadaUsuario)){
            DiseñoLabelText(AdvertenciaText,"Bien ahi, le atinaste", "green");
            textField.setDisable(true); //Ya no se puede moficar el textField
            ContadorLetrasAcertadas+=1;
            return true;
        }
        else if (!LetraCorrecta.equalsIgnoreCase(LetraIngresadaUsuario) && !LetraIngresadaUsuario.isEmpty()){ //Si las letras no coinciden y es no vacio entonces retornar falso y dejar vacio el campo de texto
            DiseñoLabelText(AdvertenciaText,"Nope,Esa no es la letra","red");
            textField.setText("");
            return false;
        }
        return true;
    }

    //Metodo para que cada vez que se presione el boton de pista se agregue la letra correspondiente en el campo en el cual se encuentre el usuario
    protected void PistaAgregarLetra(TextField textField){
        String LetraPalabraSecreta=LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        textField.setText(LetraPalabraSecreta);//Asigna al textField en el que se posicione el usuario la letra correcta
    }
    //Metodo para cambiar la imagen del sol en caso de que el usuario se equivoque
    //.toExternalForm convierte la URL a texto
    protected void CambiarSolEclipsado(){
        if(!valorDeIntento && ContadorEstadoImagenSolEclipsado<=5){
             ContadorEstadoImagenSolEclipsado+=1;
                imageSol.setImage(new Image(getClass().getResource(
                        "/com/example/soleclipsado/IMAGENES/Sol_"+ContadorEstadoImagenSolEclipsado+".png").toExternalForm()));
        }
    }

    //Metodo que cambia a la pantalla final dependiendo si el jugador gana o pierde
    protected void CambiarVistaFinal() throws IOException {

        boolean JugadorPierde = (ContadorEstadoImagenSolEclipsado==6);
        boolean JugadorGana = (ContadorEstadoImagenSolEclipsado<6 && ValidarJugadorAcertoTodasLasLetras());

        if(JugadorPierde || JugadorGana){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaFinal.fxml"));
            Parent root = fxmlLoader.load();
            FinalController finalController=fxmlLoader.getController();

            finalController.setImageSol(ContadorEstadoImagenSolEclipsado);
            if(JugadorPierde) finalController.cambiarLabelPerdedor();
            else  finalController.cambiarLabelGanador();

            Stage stage = (Stage) AnchorRoot.getScene().getWindow();
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
        }
    }

    //Metodo que retorna el valor de verdad de la igualdad entre la cantidad de letras que acerto el usuario y el numero de caracteres de la palabra secreta
    protected boolean ValidarJugadorAcertoTodasLasLetras() {

        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        return ContadorLetrasAcertadas == ListaLetrasPalabraSecreta.size();         // si el ContadorLetrasAcertadas  es igual a la cantidad de letras en ListaLetrasPalabraSecreta, retorna verdader
    }

    protected String LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(TextField textField){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        int PosicionCampodeTexto=textFields.indexOf(textField);
        return ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
    }

    //Metodo que cambia el diseño a un label , dandole un mensaje y un color
    protected void  DiseñoLabelText(Label label,String Mensaje,String Color){
        label.setStyle(label.getStyle() + "-fx-text-fill:"+Color+";");
        label.setText(Mensaje);
    }
}



