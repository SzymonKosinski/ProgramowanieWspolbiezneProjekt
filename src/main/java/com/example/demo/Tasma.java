package com.example.demo;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.chart.ScatterChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Tasma {
    private int udzwig = HelloControler.maksymalnyUdzwig;
    private int[] paramety={20,300,650,300};
    private int aktualnyUdzwig=0;
    private String pula[] = new String[100];
    private int wej = 0;
    private int wyj = 0;
    private int licznikCegiel = 0;
    private int limitCegiel = HelloControler.pojemnoscTasmy;
    public int predkoscAnimacji=HelloControler.tempoAnimacji;
    private int predkoscProdukcjiCegiel=HelloControler.tempoProdukcji;
    private List<Rectangle> ceglyNaTasmie = new ArrayList<Rectangle>();
    private List<Rectangle> ceglyNaTasmiegotowe = new ArrayList<Rectangle>();
    ReentrantLock lock = new ReentrantLock();
    ReentrantLock lock2 = new ReentrantLock();
    Condition pelny = lock.newCondition();
    Condition pusty = lock2.newCondition();


    public Tasma() {
        Line line = new Line();
        line.setManaged(false);
        line.setStartX(paramety[0]);
        line.setStartY(paramety[1]);
        line.setEndX(paramety[2]);
        line.setEndY(paramety[3]);


        Platform.runLater(() -> {
            HelloApplication.root.getChildren().add(line);
        });

    }

    public void pobierzDanezSuwakow()
    {
        predkoscAnimacji=HelloControler.tempoAnimacji;
        predkoscProdukcjiCegiel=HelloControler.tempoProdukcji;
        limitCegiel = HelloControler.pojemnoscTasmy;
        udzwig = HelloControler.maksymalnyUdzwig;

    }
    public void wstaw(String dana, int rozmiarCegly, int xCegly, int yCegly, Random random){
        pobierzDanezSuwakow();
        Rectangle rectangle = new Rectangle( xCegly-((15*rozmiarCegly)/2), (yCegly-((10*rozmiarCegly)/2)), 15*rozmiarCegly, 10*rozmiarCegly);
            try {
                lock.lock();
                while (licznikCegiel == limitCegiel || aktualnyUdzwig + rozmiarCegly >= udzwig) {
                    try {
                        pelny.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                pula[wej] = dana;
                wej = (wej + 1) % limitCegiel;
                licznikCegiel = licznikCegiel + 1;
                aktualnyUdzwig = aktualnyUdzwig + rozmiarCegly;
                animacjaWstawiania(rectangle, xCegly, yCegly);
                Thread.sleep(predkoscProdukcjiCegiel);
                System.out.printf("dodano na tasme cegle: " + dana + " liczba cegiel na tasmie: "
                        + licznikCegiel + " udzwig: " + aktualnyUdzwig + "\n");
                ceglyNaTasmie.add(rectangle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    public void sprawdzCzyMoznaPobrac()
    {
        try{
            lock2.lock();
            if(ceglyNaTasmiegotowe.size()!=0)
            {
                pusty.signalAll();
            }
        }
        finally {
            lock2.unlock();
        }
    }
    public int pobierz(Random random){
        try {
            lock2.lock();
            while (ceglyNaTasmiegotowe.size() == 0) {
                try {
                    pusty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String dana = pula[wyj];
            wyj = (wyj + 1) % limitCegiel;
            licznikCegiel = licznikCegiel - 1;
            int rozmiarCegly = Integer.parseInt(String.valueOf(dana.charAt(2)));
            aktualnyUdzwig = aktualnyUdzwig - rozmiarCegly;
            animacjaZabraniaCegly();
            ceglyNaTasmiegotowe.remove(0);
            System.out.println("zabrano z tasmy cegle: " + dana + " liczba cegiel na tasmie: "
                    + licznikCegiel + " udzwig: " + aktualnyUdzwig + "\n");
        }
        finally {
            lock2.unlock();
            new Thread(this::sprawdzCzyMoznaWstawicCegle).start();
        }
        return 1;
    }
    public void sprawdzCzyMoznaWstawicCegle()
    {
        try{
            lock.lock();
            if(licznikCegiel < limitCegiel)
            {
                pelny.signalAll();
            }
        }
        finally {
            lock.unlock();
        }
    }
    public void animacjaZabraniaCegly()
    {
        Rectangle rectangle = ceglyNaTasmiegotowe.get(0);
        Platform.runLater(()->{
            HelloApplication.root.getChildren().remove(rectangle);
        });
    }
    public void animacjaWstawiania(Rectangle rectangle, int xCegly, int yCegly)
    {
        rectangle.setFill(Color.RED);
        rectangle.setManaged(false);
        Platform.runLater(() -> {
            HelloApplication.root.getChildren().add(rectangle);
        });
        Path path = new Path();
        path.setManaged(false);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(xCegly);
        moveTo.setY(yCegly);
        LineTo lineTo = new LineTo();
        lineTo.setX(100);
        lineTo.setY(300);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(predkoscAnimacji), path, rectangle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
                animacjaNaTasmie(rectangle);
            }
        });
        Platform.runLater(pathTransition::play);


    }
    public void animacjaNaTasmie(Rectangle rectangle)
    {
        int[] daneSciezki= {100,300,650,300};
        Thread animacja = new Thread(()->{

            rectangle.setManaged(false);
            Path path = new Path();
            path.setManaged(false);
            MoveTo moveTo = new MoveTo();
            moveTo.setX(daneSciezki[0]);
            moveTo.setY(daneSciezki[1]); //zadeklarowac jako zmienne
            LineTo lineTo = new LineTo();
            lineTo.setX(daneSciezki[2]);
            lineTo.setY(daneSciezki[3]);
            path.getElements().addAll(moveTo, lineTo);
            PathTransition pathTransition = new PathTransition(Duration.millis(predkoscAnimacji), path, rectangle); //tempo animacji ma byc edytowalne
            pathTransition.setOnFinished(e -> {
                synchronized (this) {
                    ceglyNaTasmiegotowe.add(rectangle);
                    notify();
                }
            });
            Platform.runLater(pathTransition::play);

        });
        animacja.start();
    }

}
