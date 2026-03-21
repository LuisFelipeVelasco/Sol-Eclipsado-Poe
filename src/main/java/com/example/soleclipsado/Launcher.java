package com.example.soleclipsado;

import javafx.application.Application;

/**
 * Punto de entrada de la aplicación "Sol Eclipsado".
 * <p>
 * Esta clase actúa como un lanzador intermedio necesario en entornos donde
 * JavaFX no está en el classpath por defecto (por ejemplo, al empaquetar
 * con Maven sin el plugin de JavaFX). Delega el inicio a
 * {@link HelloApplication}, que es la clase que extiende {@link Application}.
 * </p>
 *
 * @author Jeronimo Rojas 2021517760-3743
 * @author Luis Felipe Velasco Chilito 2517245-3743
 * @version 1.0.4
 * @see HelloApplication
 */
public class Launcher {

    /**
     * Método principal del programa.
     * <p>
     * Lanza la aplicación JavaFX delegando a {@link Application#launch(Class, String...)}
     * con {@link HelloApplication} como clase de arranque.
     * </p>
     *
     * @param args argumentos de línea de comandos (no utilizados actualmente).
     */
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }
}
