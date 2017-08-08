package siec;

import gra.WynikStrzalu;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * Klasa, której obiekty reprezentują serwer w trybie gry sieciowej
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Serwer2 extends Thread{
    /** Strumień wejścia */
    private InputStream wejscie;
    /** Strumień wyjścia */
    private OutputStream wyjscie;
    /** Obiekt odczytujący dane z wejścia **/
    private Scanner in;
    /** Obiekt służący do wysyłania danych */
    private PrintWriter out;
    /** Obiekt klasy Serwer */
    private Serwer serwer;
    /** Obiekt służący do oczekiwania na klienta */
    private Socket incoming;
    /** Współrzędna x */
    private int x;
    /** Współrzędna y */
    private int y;
    /** Zmienna przechowująca wynik strzału */
    private WynikStrzalu wynik;
    
    /**
     * Konstruktor klasy Serwer2
     * @param serwer obiekt klasy Serwer2
     */
    public Serwer2(Serwer serwer){
        this.serwer = serwer;
    }
    
    /**
     * Metoda uruchamia wątek serwera
     * @exception Metoda może zwrócić wyjątek IOException
     */
    public void run(){
            try{
                incoming = serwer.gniazdo.accept(); 
                wejscie = incoming.getInputStream();
                wyjscie = incoming.getOutputStream();
                in = new Scanner(wejscie);
                out = new PrintWriter(wyjscie, true /*autoFlush */);
                while(in.hasNextLine()){
                    String line = in.nextLine();
                    if(line.startsWith("#$%")){
                        line = line.substring(3);
                        serwer.getZdarzenie().setChat(line);
                    }
                    else if(line.equals("zrywam"))
                        serwer.getZdarzenie().zglosZerwanieSerwerowi();
                    else if(line.equals("przerywam"))
                        serwer.getZdarzenie().przerwijSerwer();
                    else if(line.equals("jestem"))
                        serwer.getZdarzenie().zglosGotowoscKlienta();
                    else if(line.equals("start")){
                        serwer.setIsKlientStart(true);
                        if(serwer.getIsKlientStart() == true && serwer.getIsSerwerStart() == false)
                            serwer.getZdarzenie().gotowy();
                        if(serwer.getIsKlientStart() == true && serwer.getIsSerwerStart() == true)
                            serwer.getZdarzenie().startGrySerwer();
                    }
                    else if(line.startsWith("!!!")){
                        x = Integer.parseInt(line.substring(3, 5));
                        y = x % 10;
                        x = x / 10;
                        serwer.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.TRAFIONY_ZATOPIONY);
                    }
                
                    else if(line.startsWith("&&&")){
                        x = Integer.parseInt(line.substring(3, 5));
                        y = x % 10;
                        x = x / 10;
                        serwer.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, null);
                    }
                
                    else if(line.startsWith("%$#")){
                        x = Integer.parseInt(line.substring(3, 5));
                        y = x % 10;
                        x = x / 10;  
                        if(line.endsWith("TRAFIONY"))                      
                            serwer.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.TRAFIONY);
                        else{
                            serwer.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.PUDLO);
                            serwer.getZdarzenie().getGui().setToken(false);
                        }
                    }
                    
                    else if(line.equals("?")){
                        serwer.getZdarzenie().getGui().setStatkiPrzeciwnika();
                    }
                    
                    else if(line.equals("przegralem")){
                        serwer.getZdarzenie().wygralem();
                    }  
                    
                    else if(line.equals("/*/*")){
                        serwer.getZdarzenie().getGui().setToken(false);
                    }
                
                    else if(line.equals("*/*/")){
                        serwer.getZdarzenie().getGui().setToken(true);
                    }
                    
                    else if(line.startsWith("@@@")){
                        x = Integer.parseInt(line.substring(3, 5));
                        y = x % 10;
                        x = x / 10;
                        serwer.getZdarzenie().komunikatTrafiajacego(x, y);
                    }                
                    else{
                        x = Integer.parseInt(line);
                        y = x % 10;
                        x = x / 10;
                        wynik = serwer.getZdarzenie().getGui().getPoleGracza().sprawdzStrzal(x, y);
                        if(serwer.getZdarzenie().getGui().getPoleGracza().sprawdzStrzal(x, y) == WynikStrzalu.TRAFIONY_ZATOPIONY){
                            serwer.getZdarzenie().getGui().getPoleGracza().zaznaczZatopiony(x, y);
                        }
                        else{
                            serwer.getZdarzenie().getGui().getPoleGracza().oznaczPole(x, y, wynik);
                            nadaj("%$#" + x + y + wynik.toString());
                        } 
                        if(wynik == WynikStrzalu.PUDLO){
                            serwer.getZdarzenie().getGui().setToken(true);
                            serwer.nadaj("@@@" + "5" + "0");
                            serwer.getZdarzenie().komunikatTrafionego("nic", 0);
                        }
                        else{
                            Random randomGenerator = new Random();
                            String k = serwer.getZdarzenie().getGui().getPoleGracza().typ(x, y);
                            if(k.equals("Jednomasztowiec")){
                                int b = randomGenerator.nextInt(3);
                                serwer.getZdarzenie().komunikatTrafionego(k, b);
                                serwer.nadaj("@@@" + "1" + b);
                            }
                            else{
                                int q = serwer.getZdarzenie().getGui().getPoleGracza().stopien(x, y);
                                serwer.getZdarzenie().komunikatTrafionego(k, q);
                                 if(k.equals("Dwumasztowiec"))
                                    serwer.nadaj("@@@" + "2" + q);
                                else if(k.equals("Trójmasztowiec"))
                                    serwer.nadaj("@@@" + "3" + q);
                                else
                                    serwer.nadaj("@@@" + "4" + q);
                            }
                        }
                        if(serwer.getZdarzenie().getGui().getStatkiGracza() == 10){
                            nadaj("przegralem");
                            serwer.getZdarzenie().przegralem();
                        }
                    }
                }
            }catch(IOException e){
            }
    } 
    
    /**
     * Metoda wysyła wiadomość przez serwer do klienta
     * @param tekst treść wiadomości
     */    
    public void nadaj(String tekst){
        out.println(tekst);
    }
}
