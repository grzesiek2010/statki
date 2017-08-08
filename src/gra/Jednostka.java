package gra;

import java.util.ArrayList;

/**
 * Klasa, której obiekty reprezentują poszczególne statki
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Jednostka {
    /** Lista zawierająca współrzędne statku */
    private ArrayList<Wspolrzedne> lista;
    /** Zmienna przechowująca nazwę statku */
    private Flota nazwa;
    
    /**
     * Konstruktor klasy Jednostka
     * @param lista lista współrzędnych statku
     * @param nazwa nazwa statku
     */
    public Jednostka(ArrayList<Wspolrzedne> lista, Flota nazwa){
        this.lista = lista;
        this.nazwa = nazwa;
    }
    
    /**
    * Metoda zwraca listę współrzędnych statku
    * @return lista współrzędnych statku
    */
    public ArrayList<Wspolrzedne> getLista(){
        return lista;
    }
    
    /**
    * Metoda zwraca współrzędną x
    * @return współrzędna x
    */
    public int getX(int i){
        return lista.get(i).getX();
    }
     
    /**
    * Metoda zwraca współrzędną y
    * @return współrzędna y
    */
    public int getY(int i){
        return lista.get(i).getY();
    }
    
    /**
    * Metoda zwraca nazwę statku
    * @return nazwa statku
    */
    public Flota getNazwa(){
        return nazwa;
    }
}
