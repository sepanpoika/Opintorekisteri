package com.example.oh2;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TiedostonKasittely {

    public TiedostonKasittely() {

    }

    /**
     Tallennetaan kurssit tietoineen tekstitiedostoon, saadaan parametrina kurssin tiedot
     */
    public void tallennaKurssi(String kurssi, int pisteet, int arvosana) {
        try {
            if (!Files.exists(Path.of("kurssitiedot.txt"))) {
                Files.createFile(Path.of("kurssitiedot.txt"));
                System.out.println("Uusi tiedosto luotu: kurssitiedot.txt");
            } else {
                System.out.println("Tiedosto on jo olemassa: kurssitiedot.txt");
            }
        } catch (IOException e) {
            System.out.println("Tiedoston tarkistaminen epäonnistui: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("kurssitiedot.txt", true))) {
            if (Files.size(Path.of("kurssitiedot.txt")) > 0) {
                writer.newLine();
                writer.newLine();
            }
            writer.write("Kurssi: " + kurssi);
            writer.newLine();
            writer.write("Opintopisteet: " + pisteet);
            writer.newLine();
            writer.write("Arvosana: " + arvosana);

            System.out.println("Kurssitiedot tallennettu tiedostoon.");
        } catch (IOException e) {
            System.out.println("Tiedoston tallentaminen epäonnistui: " + e.getMessage());
        }
    }

    /**
     Haetaan tekstitiedostosta opintopisteiden määrä
     */
    public int getArvosana(){
        int summa = 0;

        try (BufferedReader lukija = new BufferedReader(new FileReader("kurssitiedot.txt"))) {
            String rivi;
            while ((rivi = lukija.readLine()) != null) {
                if (rivi.startsWith("Arvosana:")) {
                    String teksti = rivi.substring("Arvosana:".length()).trim();
                    int arvosanatYhteensa = Integer.parseInt(teksti);
                    summa += arvosanatYhteensa;
                }
            }
        } catch (IOException e) {
            System.out.println("Tiedoston lukeminen epäonnistui: " + e.getMessage());
        }

        return summa;
    }


    /**
     Tällä saadaan opintopisteet sovellukseen
     */
    public int getOpintopisteet(){
        int summa = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("kurssitiedot.txt"))) {
            String rivi;
            while ((rivi = reader.readLine()) != null) {
                if (rivi.startsWith("Opintopisteet:")) {
                    String teksti = rivi.substring("Opintopisteet:".length()).trim();
                    int opintopisteet = Integer.parseInt(teksti);
                    summa += opintopisteet;
                }
            }
        } catch (IOException e) {
            System.out.println("Tiedoston lukeminen epäonnistui: " + e.getMessage());
        }

        return summa;
    }


    /**
     Haetaan tekstitiedostosta kurssien määrä ja käytetään sitä keskiarvon laskemiseen
     */
    public int getKurssienmaara() {
        int kurssienMaara = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("kurssitiedot.txt"))) {
            String rivi;
            while ((rivi = reader.readLine()) != null) {
                if (rivi.startsWith("Kurssi:")) {
                    kurssienMaara++;
                }
            }
        } catch (IOException e) {
            System.out.println("Tiedoston lukeminen epäonnistui: " + e.getMessage());
        }

        return kurssienMaara;
    }

    /**
     Haetaan tekstitiedostosta kurssit
     */
    public List<String> annaKurssit() {
        List<String> kurssit = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("kurssitiedot.txt"))) {
            String rivi;
            StringBuilder kurssiBuilder = new StringBuilder();

            while ((rivi = reader.readLine()) != null) {
                if (!rivi.isEmpty()) {
                    kurssiBuilder.append(rivi.trim()).append(" ");
                } else {
                    String kurssi = kurssiBuilder.toString().trim();
                    if (!kurssi.isEmpty()) {          // only add non‑empty entries
                        kurssit.add(kurssi);
                    }
                    kurssiBuilder = new StringBuilder();
                }
            }

            if (kurssiBuilder.length() > 0) {
                String kurssi = kurssiBuilder.toString().trim();
                if (!kurssi.isEmpty()) {
                    kurssit.add(kurssi);
                }
            }
        } catch (IOException e) {
            System.out.println("Tiedoston lukeminen epäonnistui: " + e.getMessage());
        }

        return kurssit;
    }

    /**
     * Poistaa tiedostosta ensimmäisen kurssiryhmän, jonka nimi vastaa annettua arvoa.
     * Kurssit on tallennettu muodossa
     * Kurssi: <nimi>
     * Opintopisteet: <pisteet>
     * Arvosana: <arvosana>
     *
     * Ryhmien välissä on tyhjä rivi. Metodi kirjoittaa loput kurssit uudelleen tiedostoon.
     */
    public void poistaKurssi(String kurssinNimi) {
        Path path = Path.of("kurssitiedot.txt");
        if (!Files.exists(path)) {
            return; // ei tiedostoa, ei poistettavaa
        }
        try {
            List<String> lines = Files.readAllLines(path);
            List<List<String>> groups = new ArrayList<>();
            List<String> current = new ArrayList<>();
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    if (!current.isEmpty()) {
                        groups.add(new ArrayList<>(current));
                        current.clear();
                    }
                } else {
                    current.add(line);
                }
            }
            if (!current.isEmpty()) {
                groups.add(new ArrayList<>(current));
            }

            boolean removed = false;
            List<List<String>> remaining = new ArrayList<>();
            for (List<String> grp : groups) {
                boolean match = false;
                for (String l : grp) {
                    if (l.startsWith("Kurssi:") && l.substring("Kurssi:".length()).trim().equals(kurssinNimi)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    remaining.add(grp);
                } else {
                    removed = true;
                }
            }
            // kirjoita takaisin
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), false))) {
                boolean first = true;
                for (List<String> grp : remaining) {
                    if (!first) {
                        writer.newLine();
                        writer.newLine();
                    }
                    first = false;
                    for (String l : grp) {
                        writer.write(l);
                        writer.newLine();
                    }
                }
            }
            if (!removed) {
                System.out.println("Kurssia '" + kurssinNimi + "' ei löytynyt poistettavaksi.");
            }
        } catch (IOException e) {
            System.out.println("Poiston aikana tapahtui virhe: " + e.getMessage());
        }
    }

}
