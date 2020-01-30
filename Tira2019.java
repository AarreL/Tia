import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

//luokka hajautustaulun solmuille
class Solmu<K, V> { 

    K key; 
    V value; 
  
    // Viittaus seuraavaan solmuun 
    Solmu<K, V> next; 
  
    public Solmu(K key, V value) { 
        this.key = key; 
        this.value = value; 
    }

} 

//Luokka hajautustaululle
class Hajautustaulu<K, V> { 

    // Lokerotaulukko
    public ArrayList<Solmu<K, V>> lokerotaulukko; 
  
    // Lokerotaulukon kapasiteetti 
    private int kapasiteetti; 
  
    // Lokerotaulukon koko 
    private int koko; 
  
    // Rakentaja alustaa lokerotaulukon ja asettaa sen lokeroihin tyhjät sekvenssit
    public Hajautustaulu() { 
        lokerotaulukko = new ArrayList<>(); 
        kapasiteetti = 10; 
        koko = 0; 
  
        for (int i = 0; i < kapasiteetti; i++) 
            lokerotaulukko.add(null); 
    } 
  
    public int size() { 
        return koko; 
    } 
    public boolean isEmpty() { 
        return size() == 0; 
    } 
  
    // Hajautusfunktio jolla löydetään annetun avaimen indeksi
    private int getLokeroIndeksi(K key) { 
        int hashCode = key.hashCode(); 
        int index = hashCode % kapasiteetti; 
        return index; 
    } 
  
    // Avaimen poisto 
    public V remove(K key) {

        // Haetaan indeksi avaimelle 
        int lokeroIndeksi = getLokeroIndeksi(key); 
  
        // Etsitään ketjun pää 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        //tEtsitään avain ketjusta 
        Solmu<K, V> prev = null; 
        while (head != null) {

            // Jos löydetään avain 
            if (head.key.equals(key)) 
                break; 
  
            // Muutoin jatketaan ketjun läpikäyntiä
            prev = head; 
            head = head.next; 
        } 
  
        // Avainta ei löydy 
        if (head == null) 
            return null; 

        koko--; 
  
        // Poistetaan avain 
        if (prev != null) 
            prev.next = head.next; 
        else
            lokerotaulukko.set(lokeroIndeksi, head.next); 
  
        return head.value; 
    } 
  
    // Operaatio avaimeen liittyvän arvon palauttamiseen 
    public V get(K key) {

        // Haetaan annetun avainmen ketjun pää 
        int lokeroIndeksi = getLokeroIndeksi(key); 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        // Etsitään avain 
        while (head != null) 
        { 
            if (head.key.equals(key)) 
                return head.value; 
            head = head.next; 
        } 
  
        // Palautetaan null jos ei löydy 
        return null; 
    } 
  
    // Operaatio avain-arvo parin lisäämiseen 
    public void add(K key, V value) {

        // Etsitään annetun avaimen ketju 
        int lokeroIndeksi = getLokeroIndeksi(key); 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        //Mikäli avain on jo olemassa päivitetään vain arvo 
        while (head != null) { 
            if (head.key.equals(key)) 
            { 
                head.value = value; 
                return; 
            } 
            head = head.next; 
        } 
  
        // Lisätään avain ketjuun 
        koko++; 
        head = lokerotaulukko.get(lokeroIndeksi); 
        Solmu<K, V> uusiSolmu = new Solmu<K, V>(key, value); 
        uusiSolmu.next = head; 
        lokerotaulukko.set(lokeroIndeksi, uusiSolmu); 
  
        // Jos täyttöaste nousee yli 70%, kaksinkertaistetaan taulun koko
        if ((1.0*koko)/kapasiteetti >= 0.7) { 
            ArrayList<Solmu<K, V>> temp = lokerotaulukko; 
            lokerotaulukko = new ArrayList<>(); 
            kapasiteetti = 2 * kapasiteetti; 
            koko = 0; 
            for (int i = 0; i < kapasiteetti; i++) 
                lokerotaulukko.add(null); 
  
            for (Solmu<K, V> headNode : temp) { 
                while (headNode != null) 
                { 
                    add(headNode.key, headNode.value); 
                    headNode = headNode.next; 
                } 
            } 
        } 
    }
}


public class Tira2019 {
    
    //luodaan vähän hajautustauluja joukko-operaatioita varten
    static Hajautustaulu<Integer, Integer>mapOR = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Integer>ANDapu = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Integer>mapAND = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Character>aXOR = new Hajautustaulu<>();

    //Luetaan tekstitiedostosta setA.txt luvut muuttujaan yksi kerrallaan
    private void readInput() {
        try {
            File setA = new File("setA.txt");
            Scanner tiedostoA = new Scanner(setA);
            int rivi = 1;
            char tiedosto = 'A';
            while(tiedostoA.hasNextLine()) {
                int alkio = Integer.parseInt(tiedostoA.nextLine());

                //Mikäli alkiota ei ole OR-tiedostosta lukua pitävässä taulussa, lisätään se
                //Mikäli alkio on kyseisessä taulussa, päivitetään sen esiintymiskertojen lukumäärä
                //yhtä isommaksi
                if(mapOR.get(alkio) == null) {
                    mapOR.add(alkio, 1);
                } else {
                    int kpl = mapOR.get(alkio);
                    mapOR.remove(alkio);
                    mapOR.add(alkio, (kpl + 1));
                }
                
                //Tutkitaan onko alkio AND-tiedostoon liittyvässä aputaulussa, mikäli ei ole, lisätään se
                if(ANDapu.get(alkio) == null) {
                    ANDapu.add(alkio, rivi);
                }

                //Tutkitaan onko alkio ensimmäisessä XOR-tiedostoon liittyvässä taulussa, mikäli ei ole, lisätään se
                if(aXOR.get(alkio) == null) {
                    aXOR.add(alkio, tiedosto);
                }
                
                rivi++;                
            }
        } catch(IOException e) {
            System.out.println("setA.txt ei löytynyt.");
        }
        
        //Luetaan tekstitiedostosta setB.txt luvut muuttujaan yksi kerrallaan
        try {
            File setB = new File("setB.txt");
            Scanner tiedostoB = new Scanner(setB);
            int rivi;
            char tiedosto = 'B';
            while(tiedostoB.hasNextLine()) {
                int alkio = Integer.parseInt(tiedostoB.nextLine());

                //Mikäli alkiota ei ole OR-tiedostosta lukua pitävässä taulussa, lisätään se
                //Mikäli alkio on kyseisessä taulussa, päivitetään sen esiintymiskertojen lukumäärä
                //yhtä isommaksi
                if(mapOR.get(alkio) == null) {
                    mapOR.add(alkio, 1);
                } else {
                    int kpl = mapOR.get(alkio);
                    mapOR.remove(alkio);
                    mapOR.add(alkio, (kpl + 1));
                }
                
                //Tutkitaan onko alkio AND-tiedostoon liittyvässä aputaulussa, mikäli on, lisätään tiedosto
                //valmiiseen AND tauluun, sillä rivinumerolla mikä ensimmäisessä vaiheessa on annettu
                if(ANDapu.get(alkio) != null) {
                    rivi = ANDapu.get(alkio);
                    mapAND.add(alkio, rivi);
                }

                //Tutkitaan onko alkio ensimmäisessä XOR-tiedostoon liittyvässä taulussa, mikäli on, poistetaan se sieltä
                //mikäli ei ole, lisätään se sinne.
                if(aXOR.get(alkio) != null) {
                    aXOR.remove(alkio);
                }
                else {
                    aXOR.add(alkio, tiedosto);
                }
                               
            }
        } catch(IOException e) {
            System.out.println("setB.txt ei löytynyt.");
        }        
    }
    
    //Operaatio tiedostojen kirjoittamiseen
    private void writeOutput() {

        //Muuttujia kirjoittamisen avuksi
        String outputrow = "";
        int orlaskuri = 0;
        int andlaskuri = 0;
        int xorlaskuri = 0;
        Solmu<Integer, Integer> ORalkio;
        Solmu<Integer, Integer> ANDalkio;
        Solmu<Integer, Character> XORalkio;
        try {
            //Luodaan jokaiselle tiedostolle kirjoitin
            BufferedWriter bwor = new BufferedWriter(new FileWriter("or.txt"));
            BufferedWriter bwand = new BufferedWriter(new FileWriter("and.txt"));
            BufferedWriter bwxor = new BufferedWriter(new FileWriter("xor.txt"));
            

            //Kirjoitetaan or.txt siten että käydään mapOR yksitellen läpi (for silmukassa indeksit, while silmukassa
            //mahdolliset ketjutetut törmäykset) kirjoittaen kustakin alkiosta avain-arvo pari.
            for(int i = 0; i < mapOR.lokerotaulukko.size(); i++) {
                ORalkio = mapOR.lokerotaulukko.get(i);
                if(ORalkio != null) {
                    outputrow = String.format("%10d" + " - " + ORalkio.value, ORalkio.key);
                    bwor.write(outputrow);
                    bwor.newLine();
                    orlaskuri++;
                    while(ORalkio.next != null) {
                        outputrow = String.format("%10d" + " - " + ORalkio.next.value, ORalkio.next.key);
                        bwor.write(outputrow);
                        bwor.newLine();
                        ORalkio = ORalkio.next;
                        orlaskuri++;
                    }
                }
            }

            //Kirjoitetaan and.txt siten että käydään mapAND yksitellen läpi (for silmukassa indeksit, while silmukassa
            //mahdolliset ketjutetut törmäykset) kirjoittaen kustakin alkiosta avain-arvo pari.
            for(int i = 0; i < mapAND.lokerotaulukko.size(); i++) {
                ANDalkio = mapAND.lokerotaulukko.get(i);
                if(ANDalkio != null) {
                    outputrow = String.format("%10d" + " - " + ANDalkio.value, ANDalkio.key);
                    bwand.write(outputrow);
                    bwand.newLine();
                    andlaskuri++;
                    while(ANDalkio.next != null) {
                        outputrow = outputrow = String.format("%10d" + " - " + ANDalkio.next.value, ANDalkio.next.key);
                        bwand.write(outputrow);
                        bwand.newLine();
                        ANDalkio = ANDalkio.next;
                        andlaskuri++;
                    }
                }
            }

            //Kirjoitetaan xor.txt siten että käydään aXOR yksitellen läpi (for silmukassa indeksit, while silmukassa
            //mahdolliset ketjutetut törmäykset) kirjoittaen kustakin alkiosta avain-arvo pari.
            for(int i = 0; i < aXOR.lokerotaulukko.size(); i++) {
                XORalkio = aXOR.lokerotaulukko.get(i);
                if(XORalkio != null) {
                    outputrow = String.format("%10d" + " - " + XORalkio.value, XORalkio.key);
                    bwxor.write(outputrow);
                    bwxor.newLine();
                    while(XORalkio.next != null) {
                        outputrow = String.format("%10d" + " - " + XORalkio.next.value, XORalkio.next.key);
                        bwxor.write(outputrow);
                        bwxor.newLine();
                        XORalkio = XORalkio.next;
                        xorlaskuri++;
                    }
                    xorlaskuri++;
                }
            }

            //Suljetaan kirjoittajat
            bwor.close();
            bwand.close();
            bwxor.close();

            
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        //Tulostetaan viesti onnistuneesta kirjoittamisesta, sekä käyttäjälle tiedoksi lopullisten tiedostojen koko.
        System.out.println("Tiedostot kirjoitettu.");
        System.out.println("or.txt " + orlaskuri + " rows.");
        System.out.println("and.txt " + andlaskuri + " rows.");
        System.out.println("xor.txt " + xorlaskuri + " rows.");
    }
    
    public static void main(String[] args) {

        //Aloitetaan Scanner käyttäjän syötteiden lukua varten ja luodaan uusi "harjoitustyö"
        Scanner lukija = new Scanner(System.in);
        Tira2019 ht = new Tira2019();
        ht.readInput();

        boolean kysyKomento = true;

        //Käyttöliittymä, käyttäjältä kysellään komentoja kunnes komennoksi tulee lopeta
        while(kysyKomento) {
            System.out.println("Komennot:");
            System.out.println("kirjoita");
            System.out.println("poista");
            System.out.println("lopeta");
            System.out.println("");
            System.out.println("Anna komento:");
            String komento = lukija.nextLine();
            if(komento.equals("lopeta")) {
                kysyKomento = false;
                System.out.println("Näkemiin!")
            } else if(komento.equals("kirjoita")) {
                System.out.println("kirjoitetaan");
                ht.writeOutput();
            } else if(komento.equals("poista")) {
                System.out.println("Anna poistettava alkio.");
                String rivi = lukija.nextLine();
                int alkio = Integer.parseInt(rivi);
                mapOR.remove(alkio);
                mapAND.remove(alkio);
                aXOR.remove(alkio);

                System.out.println("Alkio poistettu.");
            } else {
                System.out.println("Virheellinen komento.");
            }
            System.out.println("");
        }
    }
}