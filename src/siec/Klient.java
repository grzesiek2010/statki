package siec;

import java.io.IOException;
import java.net.Socket;

/**
 * Klasa, której obiekty reprezentują klienta w trybie gry sieciowej
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Klient {
    /** Zmienna zawierająca adres serwera */
    private String host;
    /** Zmienna zawierająca port serwera */
    private int port;
    /** Zmienna świadcząca o połączeniu z serwerem */
    private boolean isConnect;
    /** Obiekt klasy Klient2 */
    private Klient2 klient;
    /** Gniazdo sieciowe */
    private Socket gniazdo;
    /** Obiekt klasy Zdarzenie */
    private Zdarzenia zdarzenie;
    /** Zmienna Świadcząca o rozpoczęciu gry przez klienta */
    private boolean isKlientStart;
    /** Zmienna Świadcząca o rozpoczęciu gry przez serwer */
    private boolean isSerwerStart;
    
    /**
     * Konstruktor klasy Klient
     * @param host adres serwera
     * @param port port na którym działa serwer
     * @param zdarzenie obiekt klasy Zdarzenie
     */
    public Klient(String host, int port, Zdarzenia zdarzenie){
        this.host = host;
        this.port = port;
        this.zdarzenie = zdarzenie;
        isConnect = false;
        isKlientStart = false;
        isSerwerStart = false;
    }
    
    /**
     * Metoda uruchamia metodę uruchamiającą wątek klienta
     * @return true jeśli udało się nawiązać połączenie, w przeciwnym razie false
     * @exception Metoda może zwrócić wyjątek IOException
     */
    public boolean Start(){
        boolean test;
        isConnect = true;
        try{
            gniazdo = new Socket(host,port);
            klient = new Klient2(this);
            klient.start();
            test = true;
        }catch(IOException e){
            test = false;
        }        
        return test;
    }
    
    /**
     * Metoda przerywa wątek klienta
     * @exception Metoda może zwrócić wyjątek IOException
     */
    public void Stop(){
        klient.interrupt();
        try{
            gniazdo.close();
        }catch(IOException e){
        }
        isConnect = false;
        isKlientStart = false;
        isSerwerStart = false;
    }
    
    /**
     * Metoda zwraca stan połączenia klienta z serwerem
     * @return treu jeśli połączono, w przeciwnym razie false
     */
    public boolean getIsConnect(){
        return isConnect;
    }
    
    /**
     * Metoda zmienia stan połączenia klienta z serweram
     * @param test stan połaczenia
     */
    public void setIsConnect(boolean test){
        isConnect = test;
    } 
    
    /**
     * Metoda wywołuje metodę wysyłającą wiadomośc przez klienta do serwera
     * @param tekst treść wiadomości
     */
    public void nadaj(String tekst){
        klient.nadaj(tekst);
    }
    
    /**
     * Metoda zwraca obiekt klasy Socket
     * @return obiekt klasy Socket
     */
    public Socket getGniazdo(){
        return gniazdo;
    }
    
    /**
     * Metoda zwraca stan gry klienta start/stop
     * @return true jeśli klient rozpoczą grę, w przeciwnym razie false
     */
    public boolean getIsKlientStart(){
        return isKlientStart;
    }
    
    /**
     * Metoda zmienia stan gry klienta pomiędzy start/stop
     * @param test stan gry klienta start/stop
     */    
    public void setIsKlientStart(boolean test){
        isKlientStart = test;
    }

    /**
     * Metoda zwraca stan gry serwera start/stop
     * @return true jeśli serwer rozpoczą grę, w przeciwnym razie false
     */
    public boolean getIsSerwerStart(){
        return isSerwerStart;
    }
    
    /**
     * Metoda zmienia stan gry serwera pomiędzy start/stop
     * @param test stan gry serwera start/stop
     */
    public void setIsSerwerStart(boolean test){
        isSerwerStart = test;
    }    
    
    /**
     * Metoda zwraca obiekt klasy Zdarzenie
     * @return obiekt klasy Zdarzenie
     */
    public Zdarzenia getZdarzenie(){
        return zdarzenie;
    }
}
