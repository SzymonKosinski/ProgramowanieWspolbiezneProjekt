package com.example.demo;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;


public class Ciezarowka extends Thread{
    private int ladownosc=HelloControler.limitCiezarowki;
    private final Tasma monitor;
    private final int x,y, width, height;
    private float objetoscCegiel;
    private Rectangle zapelnienie;
    private int senwatku=10;


    public Ciezarowka(Tasma monitor, int x, int y, int width, int height) {
        this.monitor = monitor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void run() {
        int ladownoscAktualna=ladownosc;
        Random random = new Random();
        Rectangle rectangle = new Rectangle(x,y,width, height);
        rectangle.setManaged(false);
        rectangle.setFill(Color.GRAY);
        Platform.runLater(() -> {
            HelloApplication.root.getChildren().add(rectangle);
        });
        while (true){
            new Thread(() ->{
                while (true){
                    try {
                        Thread.sleep(senwatku);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    monitor.sprawdzCzyMoznaPobrac(); }
            }).start();
            ladownosc=HelloControler.limitCiezarowki;
            ladownoscAktualna=ladownoscAktualna-monitor.pobierz(random);

            System.out.printf("pozostala ladownosc ciezarowki wynosi: " + ladownoscAktualna+ "\n");
            if(ladownoscAktualna-1<0)
            {
                ladownosc=HelloControler.limitCiezarowki;
                System.out.println("nowa ciezarowka podjechala\n");
                Platform.runLater(() -> {
                    HelloApplication.root.getChildren().remove(rectangle);
                    HelloApplication.root.getChildren().add(rectangle);
                });
                ladownoscAktualna=ladownosc;
            }
            else
            {
                Platform.runLater(() -> {
                    HelloApplication.root.getChildren().remove(zapelnienie);
                });
                objetoscCegiel=(float) (ladownosc-ladownoscAktualna)/ladownosc;
                zapelnienie = new Rectangle(x,y,width*objetoscCegiel,height*objetoscCegiel);
                zapelnienie.setManaged(false);
                zapelnienie.setFill(Color.RED);
                Platform.runLater(() -> {
                    HelloApplication.root.getChildren().add(zapelnienie);
                });
            }
        }

    }
}
