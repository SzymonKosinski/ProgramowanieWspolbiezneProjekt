package com.example.demo;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;
public class Pracownik extends Thread{

        private final int id;
        private final Tasma monitor;
        private final int x,y, width, height;


        public Pracownik(int id, Tasma monitor, int x, int y, int width, int height) {
            this.id = id;
            this.monitor = monitor;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }


        public void run() {

            Random random = new Random();
            Rectangle rectangle = new Rectangle(x,y,width, height);
            rectangle.setManaged(false);
            rectangle.setFill(Color.GRAY);
            Platform.runLater(() -> {
                HelloApplication.root.getChildren().add(rectangle);
            });
            while(true){
                    String dana ="P-" + id;
                    monitor.wstaw(dana, id, x+(width/2), y+(height/2), random);

            }
        }
    }
