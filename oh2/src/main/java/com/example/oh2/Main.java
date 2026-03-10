package com.example.oh2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private VBox root;
    private VBox coursePane; // container for the list of courses
    private List<HBox> kurssikentat = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        TextField kurssiInput = new TextField();
        TextField opintoInput = new TextField();
        TextField pisteInput = new TextField();
        Button addButton = new Button("Lisää");
        Label opintomaara = new Label();
        Label keskiarvo = new Label();
        HBox eka = new HBox();
        eka.setSpacing(10);
        double temp;
        double arvosana2;

        primaryStage.setTitle("Opinnonseurantasovellus");

        Label course = new Label("Kurssin nimi:");
        Label credit = new Label("Opintopisteet:");
        Label grade = new Label("Arvosana:");

        kurssiInput.setMaxWidth(150);
        opintoInput.setMaxWidth(35);
        pisteInput.setMaxWidth(35);
        root = new VBox();

        eka.getChildren().addAll(course, kurssiInput, credit, opintoInput, grade, pisteInput, addButton);

       // lisäysnappi
        addButton.setOnAction(event -> {
            String kurssinNimi = kurssiInput.getText();
            String opintopisteet = opintoInput.getText();
            String arvosanaa = pisteInput.getText();
            Olioluokka olioluokka = new Olioluokka(kurssinNimi, Integer.parseInt(opintopisteet), Integer.parseInt(arvosanaa));
            olioluokka.tallennetaan();

            // päivitykset UI:hin
            updateStatistics(opintomaara, keskiarvo);
            refreshCourseList(opintomaara, keskiarvo);

            // tyhjennetään kentät
            kurssiInput.clear();
            opintoInput.clear();
            pisteInput.clear();
        });

        root.setSpacing(10);
        // add input row and stats labels, then a dedicated pane for the course list
        root.getChildren().addAll(eka);

        // alustetaan tilastot ja kurssilista
        updateStatistics(opintomaara, keskiarvo);
        root.getChildren().add(opintomaara);
        root.getChildren().add(keskiarvo);

        coursePane = new VBox();
        coursePane.setSpacing(10);
        root.getChildren().add(coursePane);

        refreshCourseList(opintomaara, keskiarvo);

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    /**
     * Päivittää opintopisteiden määrän ja keskiarvon labelit.
     */
    private void updateStatistics(Label opintomaara, Label keskiarvo) {
        TiedostonKasittely hae = new TiedostonKasittely();
        opintomaara.setText("Opintopisteiden määrä: " + String.valueOf(hae.getOpintopisteet()));
        double pisteet = hae.getArvosana();
        double temp = hae.getKurssienmaara() == 0 ? 0 : (double) pisteet / hae.getKurssienmaara();
        keskiarvo.setText("Kurssiesi keskiarvo on: " + temp);
    }

   // päivitetään kurssilistat ja poistonapin toiminnallisuus
    private void refreshCourseList(Label opintomaara, Label keskiarvo) {
        TiedostonKasittely tiedot = new TiedostonKasittely();
        List<String> kurssit = tiedot.annaKurssit();

        VBox kurssiRivit = new VBox();
        kurssiRivit.setSpacing(10);

        kurssikentat.clear();
        for (String kurssi : kurssit) {
            if (kurssi.trim().isEmpty()) {
                continue; 
            }

            Label label = new Label(kurssi);
            Button remove = new Button("Poista");
            HBox rivi = new HBox(5, label, remove);

            // napin käsittelijä
            remove.setOnAction(e -> {
                // yritetään poistaa kurssi tiedostosta
                String nimi = kurssi;
                if (nimi.startsWith("Kurssi:")) {
                    nimi = nimi.substring("Kurssi:".length()).trim();
                    int endIdx = nimi.indexOf("Opintopisteet:");
                    if (endIdx != -1) {
                        nimi = nimi.substring(0, endIdx).trim();
                    }
                }
                if (!nimi.isEmpty()) {
                    TiedostonKasittely käs = new TiedostonKasittely();
                    käs.poistaKurssi(nimi);
                }
                // päivitetään näkymä
                updateStatistics(opintomaara, keskiarvo);
                refreshCourseList(opintomaara, keskiarvo);
            });

            kurssiRivit.getChildren().add(rivi);
            kurssikentat.add(rivi);
        }

        // show the rows inside the dedicated pane
        if (coursePane == null) {
            coursePane = new VBox();
            coursePane.setSpacing(10);
            root.getChildren().add(coursePane);
        }
        coursePane.getChildren().setAll(kurssiRivit.getChildren());
    }

    // sovelluksen käynnistys
    public static void main(String[] args) {
        launch(args);
    }
}




