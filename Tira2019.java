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


class Solmu<K, V> 
{ 
    K key; 
    V value; 
  
    // Viittaus seuraavaan solmuun 
    Solmu<K, V> next; 
  
    public Solmu(K key, V value) 
    { 
        this.key = key; 
        this.value = value; 
    }

} 

// Oma hajautustaulu-luokka
class Hajautustaulu<K, V> 
{ 
    // Lokerotaulukko
    public ArrayList<Solmu<K, V>> lokerotaulukko; 
  
    // Lokerotaulukon kapasiteetti 
    private int kapasiteetti; 
  
    // Lokerotaulukon koko 
    private int koko; 
  
    // Rakentaja alustaa lokerotaulukon ja asettaa sen lokeroihin tyhjät sekvenssit
    public Hajautustaulu() 
    { 
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
  
    // This implements hash function to find index 
    // for a key 
    private int getLokeroIndeksi(K key) 
    { 
        int hashCode = key.hashCode(); 
        int index = hashCode % kapasiteetti; 
        return index; 
    } 
  
    // Method to remove a given key 
    public V remove(K key) 
    { 
        // Apply hash function to find index for given key 
        int lokeroIndeksi = getLokeroIndeksi(key); 
  
        // Get head of chain 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        // Search for key in its chain 
        Solmu<K, V> prev = null; 
        while (head != null) 
        { 
            // If Key found 
            if (head.key.equals(key)) 
                break; 
  
            // Else keep moving in chain 
            prev = head; 
            head = head.next; 
        } 
  
        // If key was not there 
        if (head == null) 
            return null; 
  
        // Reduce size 
        koko--; 
  
        // Remove key 
        if (prev != null) 
            prev.next = head.next; 
        else
            lokerotaulukko.set(lokeroIndeksi, head.next); 
  
        return head.value; 
    } 
  
    // Returns value for a key 
    public V get(K key) 
    { 
        // Find head of chain for given key 
        int lokeroIndeksi = getLokeroIndeksi(key); 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        // Search key in chain 
        while (head != null) 
        { 
            if (head.key.equals(key)) 
                return head.value; 
            head = head.next; 
        } 
  
        // If key not found 
        return null; 
    } 
  
    // Adds a key value pair to hash 
    public void add(K key, V value) 
    { 
        // Find head of chain for given key 
        int lokeroIndeksi = getLokeroIndeksi(key); 
        Solmu<K, V> head = lokerotaulukko.get(lokeroIndeksi); 
  
        // Check if key is already present 
        while (head != null) 
        { 
            if (head.key.equals(key)) 
            { 
                head.value = value; 
                return; 
            } 
            head = head.next; 
        } 
  
        // Insert key in chain 
        koko++; 
        head = lokerotaulukko.get(lokeroIndeksi); 
        Solmu<K, V> uusiSolmu = new Solmu<K, V>(key, value); 
        uusiSolmu.next = head; 
        lokerotaulukko.set(lokeroIndeksi, uusiSolmu); 
  
        // If load factor goes beyond threshold, then 
        // double hash table size 
        if ((1.0*koko)/kapasiteetti >= 0.7) 
        { 
            ArrayList<Solmu<K, V>> temp = lokerotaulukko; 
            lokerotaulukko = new ArrayList<>(); 
            kapasiteetti = 2 * kapasiteetti; 
            koko = 0; 
            for (int i = 0; i < kapasiteetti; i++) 
                lokerotaulukko.add(null); 
  
            for (Solmu<K, V> headNode : temp) 
            { 
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
    
    static Hajautustaulu<Integer, Integer>mapOR = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Integer>ANDapu = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Integer>mapAND = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Character>aXOR = new Hajautustaulu<>();
    static Hajautustaulu<Integer, Character>bXOR = new Hajautustaulu<>();


    /*ei toimi tällä hetkellä
    private static void tulostaA() {
        int lkm = 0;
        System.out.println("Alkio - lukumäärä joukossa");
        for(int i = 0; i < mapAkpl.lokerotaulukko.size(); i++) {
            if(mapAkpl.lokerotaulukko.get(i) != null) {
                System.out.println(mapAkpl.lokerotaulukko.get(i).key + " - " + mapAkpl.lokerotaulukko.get(i).value);
                lkm += mapAkpl.lokerotaulukko.get(i).value;
            }
        }
        System.out.println("Alkioita yhteensä: " + lkm);
    }*/
    
    /*ei toimi tällä hetkellä
    private static void tulostaB() {
        int lkm = 0;
        System.out.println("Alkio - lukumäärä joukossa");
        for(int i = 0; i < mapBkpl.lokerotaulukko.size(); i++) {
            if(mapBkpl.lokerotaulukko.get(i) != null) {
                System.out.println(mapBkpl.lokerotaulukko.get(i).key + " - " + mapBkpl.lokerotaulukko.get(i).value);
                lkm += mapBkpl.lokerotaulukko.get(i).value;
            }
        }
        System.out.println("Alkioita yhteensä: " + lkm);
    }*/
    
    private void readInput() {
        try {
            File setA = new File("setA.txt");
            Scanner tiedostoA = new Scanner(setA);
            int rivi = 1;
            char tiedosto = 'A';
            while(tiedostoA.hasNextLine()) {
                int alkio = Integer.parseInt(tiedostoA.nextLine());
                if(mapOR.get(alkio) == null) {
                    mapOR.add(alkio, 1);
                } else {
                    int kpl = mapOR.get(alkio);
                    mapOR.remove(alkio);
                    mapOR.add(alkio, (kpl + 1));
                }
                
                if(ANDapu.get(alkio) == null) {
                    ANDapu.add(alkio, rivi);
                }

                if(aXOR.get(alkio) != null) {
                    aXOR.add(alkio, tiedosto);
                }
                
                rivi++;                
            }
        } catch(IOException e) {
            System.out.println("setA.txt not found.");
        }
        
        try {
            File setB = new File("setB.txt");
            Scanner tiedostoB = new Scanner(setB);
            int rivi;
            char tiedosto = 'B';
            while(tiedostoB.hasNextLine()) {
                int alkio = Integer.parseInt(tiedostoB.nextLine());
                if(mapOR.get(alkio) == null) {
                    mapOR.add(alkio, 1);
                } else {
                    int kpl = mapOR.get(alkio);
                    mapOR.remove(alkio);
                    mapOR.add(alkio, (kpl + 1));
                }
                
                if(ANDapu.get(alkio) != null) {
                    rivi = ANDapu.get(alkio);
                    mapAND.add(alkio, rivi);
                }
                if(aXOR.get(alkio) == null) {
                    bXOR.add(alkio, tiedosto);
                }
                else {
                    aXOR.remove(alkio);
                }
                               
            }
        } catch(IOException e) {
            System.out.println("setB.txt not found.");
        }        
    }
    
    private void writeOutput() {

        String outputrow = "";
        int orlaskuri = 0;
        int andlaskuri = 0;
        int xorlaskuri = 0;
        try {
            BufferedWriter bwor = new BufferedWriter(new FileWriter("or.txt"));
            BufferedWriter bwand = new BufferedWriter(new FileWriter("and.txt"));
            BufferedWriter bwxor = new BufferedWriter(new FileWriter("xor.txt"));
            

            for(int i = 0; i < mapOR.lokerotaulukko.size(); i++) {
                if(mapOR.lokerotaulukko.get(i) != null) {
                    outputrow = mapOR.lokerotaulukko.get(i).key + " - " + mapOR.lokerotaulukko.get(i).value;
                    bwor.write(outputrow);
                    bwor.newLine();
                    orlaskuri++;
                }
            }

            for(int i = 0; i < mapAND.lokerotaulukko.size(); i++) {
                if(mapAND.lokerotaulukko.get(i) != null) {
                    outputrow = mapAND.lokerotaulukko.get(i).key + " - " + mapAND.lokerotaulukko.get(i).value;
                    bwand.write(outputrow);
                    bwand.newLine();
                    andlaskuri++;
                }
            }

            for(int i = 0; i < aXOR.lokerotaulukko.size(); i++) {
                if(aXOR.lokerotaulukko.get(i) != null) {
                    outputrow = aXOR.lokerotaulukko.get(i).key + " - " + aXOR.lokerotaulukko.get(i).value;
                    bwxor.write(outputrow);
                    bwxor.newLine();
                    xorlaskuri++;
                }
            }

            for(int i = 0; i < bXOR.lokerotaulukko.size(); i++) {
                if(bXOR.lokerotaulukko.get(i) != null) {
                    outputrow = bXOR.lokerotaulukko.get(i).key + " - " + bXOR.lokerotaulukko.get(i).value;
                    bwxor.write(outputrow);
                    bwxor.newLine();
                    xorlaskuri++;
                }
            }

            
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        System.out.println("Writing files...");
        System.out.println("Files written.");
        System.out.println("or.txt " + orlaskuri + " rows.");
        System.out.println("and.txt " + andlaskuri + " rows.");
        System.out.println("xor.txt " + xorlaskuri + " rows.");
    }
    
    public static void main(String[] args) {

        Scanner lukija = new Scanner(System.in);
        Tira2019 ht = new Tira2019();
        ht.readInput();

        boolean kysyKomento = true;

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
                bXOR.remove(alkio);
                System.out.println("Alkio poistettu.");
            } else {
                System.out.println("Virheellinen komento.");
            }
            System.out.println("");
        }
    }
}