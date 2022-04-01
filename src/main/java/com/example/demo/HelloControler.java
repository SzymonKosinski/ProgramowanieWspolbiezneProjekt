package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class HelloControler {
    static int tempoAnimacji=1000;
    static int tempoProdukcji=500;
    static int limitCiezarowki=5;
    static int pojemnoscTasmy=10;
    static int maksymalnyUdzwig=20;
    @FXML
    private Label text1;

    @FXML
    private Button okBtn;

    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    private javafx.scene.control.Slider predkoscAnimacji;
    @FXML
    private javafx.scene.control.Slider predkoscProdukcji;
    @FXML
    private javafx.scene.control.Slider ciezarowka;
    @FXML
    private javafx.scene.control.Slider pojemnosc;
    @FXML
    private javafx.scene.control.Slider udzwig;

    @FXML
    public void onSliderChanged() {
        tempoAnimacji = (int) predkoscAnimacji.getValue();
        tempoProdukcji = (int) predkoscProdukcji.getValue();
        limitCiezarowki = (int) ciezarowka.getValue();
        pojemnoscTasmy = (int) pojemnosc.getValue();
        maksymalnyUdzwig = (int) udzwig.getValue();

    }

    @FXML
    protected void onOkButtonAction() {
        text1.setVisible(true);
        text1.setText("Moj tekst");
    }

    @FXML
    protected void onButtonAction() {
        okBtn.setDisable(true);
        text1.setVisible(false);
    }


}