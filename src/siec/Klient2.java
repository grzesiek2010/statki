package siec;

import gra.WynikStrzalu;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Klasa, której obiekty reprezentują klienta w trybie gry sieciowej
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Klient2 extends Thread{
    /** Obiekt klasy Klient */
    private Klient klient;
    /** Strumień wejścia */
    private InputStream wejscie;
    /** Strumień wyjścia */
    private OutputStream wyjscie;
    /** Obiekt odczytujący dane z wejścia **/
    private Scanner in;
    /** Obiekt służący do wysyłania danych */
    private PrintWriter out;
    /** Współrzędna x */
    private int x;
    /** Współrzędna y */
    private int y;
    /** Zmienna przechowująca wynik strzału */
    private WynikStrzalu wynik;
    
    /**
     * Konstruktor klasy Klient2
     * @param klient obiekt klasy Klient
     * @exception Metoda może zwrócić wyjątek IOException
     */
    public Klient2(Klient klient){
        this.klient = klient;
        try{
            wejscie = klient.getGniazdo().getInputStream();
            wyjscie = klient.getGniazdo().getOutputStream();
            in = new Scanner(wejscie);
            out = new PrintWriter(wyjscie, true /*autoFlush */);          
        }catch(IOException e){
        }        
    }
    
    /**
     * Metoda uruchamia wątek klienta
     */
    public void run(){
            while(in.hasNextLine()){
                String line = in.nextLine();
                if(line.startsWith("#$%")){
                    line = line.substring(3);
                    klient.getZdarzenie().setChat(line);
                }                
                else if(line.equals("zrywam"))
                    klient.getZdarzenie().zglosZerwanieKlientowi();
                
                else if(line.equals("przerywam"))
                    klient.getZdarzenie().przerwijKlient();
                    
                else if(line.equals("start")){
                    klient.setIsSerwerStart(true);
                    if(klient.getIsKlientStart() == false && klient.getIsSerwerStart() == true)
                        klient.getZdarzenie().gotowy();
                    if(klient.getIsKlientStart() == true && klient.getIsSerwerStart() == true)
                        klient.getZdarzenie().startGryKlient();                    
                }
                
                else if(line.startsWith("!!!")){
                    x = Integer.parseInt(line.substring(3, 5));
                    y = x % 10;
                    x = x / 10;
                    klient.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.TRAFIONY_ZATOPIONY);
                }
                
                else if(line.startsWith("&&&")){
                    x = Integer.parseInt(line.substring(3, 5));
                    y = x % 10;
                    x = x / 10;
                    klient.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, null);
                }
                
                else if(line.equals("/*/*")){
                    klient.getZdarzenie().getGui().setToken(false);
                }
                
                else if(line.equals("*/*/")){
                    klient.getZdarzenie().getGui().setToken(true);
                }
                
                else if(line.startsWith("%$#")){
                    x = Integer.parseInt(line.substring(3, 5));
                    y = x % 10;
                    x = x / 10;  
                    if(line.endsWith("TRAFIONY"))    {                  
                        klient.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.TRAFIONY);
                        
                    }
                    else{
                        klient.getZdarzenie().getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.PUDLO);
                        klient.getZdarzenie().getGui().setToken(false);
                    }
                }
                
                else if(line.equals("?")){
                    klient.getZdarzenie().getGui().setStatkiPrzeciwnika();
                }
     
                else if(line.equals("przegralem")){
                    klient.getZdarzenie().wygralem();
                }
                else if(line.startsWith("@@@")){
                     x = Integer.parseInt(line.substring(3, 5));
                     y = x % 10;
                     x = x / 10;
                     klient.getZdarzenie().komunikatTrafiajacego(x, y);
                }                
                else{
                    x = Integer.parseInt(line);
                    y = x % 10;
                    x = x / 10;
                    wynik = klient.getZdarzenie().getGui().getPoleGracza().sprawdzStrzal(x, y);
                    if(klient.getZdarzenie().getGui().getPoleGracza().sprawdzStrzal(x, y) == WynikStrzalu.TRAFIONY_ZATOPIONY){
                        klient.getZdarzenie().getGui().getPoleGracza().zaznaczZatopiony(x, y);
                    }
                    else{
                        klient.getZdarzenie().getGui().getPoleGracza().oznaczPole(x, y, wynik);
                        nadaj("%$#" + x + y + wynik.toString());
                    }
                    if(wynik == WynikStrzalu.PUDLO){
                        klient.getZdarzenie().getGui().setToken(true);
                        klient.nadaj("@@@" + "5" + "0");
                        klient.getZdarzenie().komunikatTrafionego("nic", 0);
                    }
                    else{
                        Random randomGenerator = new Random();
                        String k = klient.getZdarzenie().getGui().getPoleGracza().typ(x, y);
                        if(k.equals("Jednomasztowiec")){
                            int b = randomGenerator.nextInt(3);
                            klient.getZdarzenie().komunikatTrafionego(k, b);
                            klient.nadaj("@@@" + "1" + b);

                        }
                        else{
                            int q = klient.getZdarzenie().getGui().getPoleGracza().stopien(x, y);
                                klient.getZdarzenie().komunikatTrafionego(k, q);
                                 if(k.equals("Dwumasztowiec"))
                                    klient.nadaj("@@@" + "2" + q);
                                 else if(k.equals("Trójmasztowiec"))
                                    klient.nadaj("@@@" + "3" + q);
                                 else 
                                    klient.nadaj("@@@" + "4" + q);
                        }
                    }
                        if(klient.getZdarzenie().getGui().getStatkiGracza() == 10){
                            nadaj("przegralem");
                            klient.getZdarzenie().przegralem();
                        }
                }
            }  
    }
    
    /**
     * Metoda wysyła wiadomość przez klienta do serwera
     * @param tekst treść wiadomości
     */
    public void nadaj(String tekst){
        out.println(tekst);
    }
}
