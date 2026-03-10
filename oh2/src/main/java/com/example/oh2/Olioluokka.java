package com.example.oh2;


/**
 Sovelluksen olioluokka
 */
public class Olioluokka {
    /**
     Luodaan oliomuuttujat, jotka tallentavat kurssin nimen, opintopisteet ja arvosanan
     */
    private String nimi;
    private int opintopisteet;
    private int arvosana;

    /**
     Luodaan Olioluokkan konstruktori, saadaan paramterina kurssitietoja
     */
    public Olioluokka(String kurssinNimi, int pisteet, int arvio) {
        nimi = kurssinNimi;
        opintopisteet = pisteet;
        arvosana = arvio;
    }

    /**
     Tätä metodia kutsumalla saadaan opintopisteiden määrä kokonaisuudessaan
     */
    public int opintopisteet() {
        TiedostonKasittely maara = new TiedostonKasittely();
        return maara.getOpintopisteet();
    }


    public void tallennetaan() {
        /**
         Tallennetaan juuri saadut kurssitiedot tekstitiedostoon kurssitiedot.txt
         */
        TiedostonKasittely temp = new TiedostonKasittely();
        temp.tallennaKurssi(this.nimi, this.opintopisteet, arvosana);
    }


    @Override
    public String toString() {
        return "Olioluokka: nimi = " + nimi + ", opintopisteet = " + opintopisteet + ", arvosana = " + arvosana;
    }
}
