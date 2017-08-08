package siec;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Klasa, której obiekty reprezentują serwer w trybie gry sieciowej
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Serwer{
    /** Zmienna zawierająca port serwera */
    private int port;
    /** Gniazdo sieciowe */
    public ServerSocket gniazdo;
    /** Zmienna świadcząca o uruchomieniu serwera */
    private boolean isRunning;
    /** Zmienna śwaidcząca o połączeniu klienta */
    private boolean isConnect;
    /** Obiekt klasy Serwer2 */
    public Serwer2 serwer;
    /* Obiekt klasy Zdarzenie */
    private Zdarzenia zdarzenie;
    /** Zmienna Świadcząca o rozpoczęciu gry przez klienta */
    private boolean isKlientStart;
    /** Zmienna Świadcząca o rozpoczęciu gry przez klienta */
    private boolean isSerwerStart;
    
    /**
     * Konstruktor klasy Serwer
     * @param port port serwera
     * @param zdarzenie obiekt klasy Zdarzenie
     */
    public Serwer(int port, Zdarzenia zdarzenie){
        this.port = port;
        isRunning = false;
        isConnect = false;
        isKlientStart = false;
        isSerwerStart = false;
        this.zdarzenie = zdarzenie;
    }

        /**
         * Metoda uruchamia  metodę uruchamiającą wątek serwera
         * @return true, jesli udało się uruchomić serwer, w przeciwnym razie false
         * @exception Metoda może zwrócić wyjątek Exception
         */
	public boolean Start() {
		try {
			gniazdo = new ServerSocket(port);
		} catch (Exception ex) {
			return false;
		}
		isRunning = true;
		serwer = new Serwer2(this);
		serwer.start();
		return true;
	}
        
        /**
         * Metoda przerywa wątek serwera
         * @exception Metoda może zwrócić wyjątek IOException
         */
	public void Stop() {
		isRunning = false;
                isKlientStart = false;
                isSerwerStart = false;
                isConnect = false;
		serwer.interrupt();
		try {
                    gniazdo.close();
		} catch (IOException ex) {
		}
	}
        
        /**
         * Metoda zwraca stan serwera włączony/wyłączony
         * @return true jeśli serwer jest włączony, w przeciwnym razie false
         */
	public boolean isRunning() {
		return isRunning;
	}
        
        /**
        * Metoda zwraca stan połączenia klienta z serwerem
        * @return treu jeśli połączono, w przeciwnym razie false
        */
        public boolean isConnect(){
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
        * Metoda wywołuje metodę wysyłającą wiadomośc przez serwer do klienta
        * @param tekst treść wiadomości
        */
        public void nadaj(String tekst){
            serwer.nadaj(tekst);
        }
        
        /**
        * Metoda zwraca obiekt klasy Zdarzenie
        * @return obiekt klasy Zdarzenie
        */        
        public Zdarzenia getZdarzenie(){
            return zdarzenie;
        }
}
