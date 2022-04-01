package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    static Pane root;

    @Override
    public void start(Stage stage) throws IOException {
        final int[] scenaWymiary = {800,600};
        final int[] wspolrzednePracownika = {10,80};
        final int[] xPracownika={50,250,450};
        final int[] wspolrzedneCiezarowki= {655,250,100};
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root, scenaWymiary[0], scenaWymiary[1]);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
        Tasma monitor = new Tasma();
        Pracownik p1= new Pracownik(1, monitor, xPracownika[0],wspolrzednePracownika[0],wspolrzednePracownika[1],wspolrzednePracownika[1]);
        Pracownik p2= new Pracownik(2, monitor,xPracownika[1],wspolrzednePracownika[0],wspolrzednePracownika[1],wspolrzednePracownika[1]);
        Pracownik p3= new Pracownik(3, monitor,xPracownika[2],wspolrzednePracownika[0],wspolrzednePracownika[1],wspolrzednePracownika[1] );
        Ciezarowka ciezarowka = new Ciezarowka( monitor, wspolrzedneCiezarowki[0], wspolrzedneCiezarowki[1], wspolrzedneCiezarowki[2], wspolrzedneCiezarowki[2]);

        p1.start();
        p2.start();
        p3.start();
        ciezarowka.start();
    }

    public static void main(String[] args) {
        launch();
    }
}