package siec;

import gra.GUI;
import java.awt.Color;

/**
 * Klasa służy do obsługi zdarzeń
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Zdarzenia {

/** Zmienna przechowująca domyslny komunikat okna dialogowego */
private String tekst = "Witaj w grze w statki";
/** Zmienna przechowująca domyslny kolor tła okna dialogowego */
private Color kolor = new Color(255,250,205);
/** Obiekt klasy Serwer */
private Serwer serwer = null;
/** Obiekt klasy Klient */
private Klient klient = null;
/** Obiekt klasy GUI */
private GUI gui;

    public Zdarzenia(GUI gui){
        this.gui = gui;  
    }
    
    /**
     * Metoda zmienia kolor jaki ma przyjąć tło okna dialogowgo
     * @param barwa kolor tła okna
     */
    public void setColor(Color barwa){
        kolor = barwa;
    }
    
    /**
     * Metoda zmienia treść komunikatu jaki ma pojawić się w oknie dialogowym
     * @param tresc terść komunikatu
     */
    public void setTekst(String tresc){
        tekst = tresc;
    }
    
    /**
     * Mtoda zwraca kolor jaki ma przyjąc tło okna dialogowego
     * @return kolor tła okna
     */
    public Color getKolor(){
        return kolor;
    }
    
    /**
     * Metoda zwraca treść komunikatu jaki ma pojawić się w oknie dialogowym
     * @return treść komunikatu
     */
    public String getTekst(){
        return tekst;
    }
    
    /**
     * Metoda uruchamia serwer
     * @param port port na którymma działać serwer
     * @return true jeśli serwer został poprawnie uruchomiony, w przeciwnym razie false
     */
    public boolean uruchomSerwer(int port){
        boolean test;
        gui.setSerw(true);
        serwer = new Serwer(port, this);
        if(serwer.Start()){
            setTekst("Serwer włączony! Trwa oczekiwanie na przeciwnika.");
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);
            test = true;
        }
        else{
            setTekst("Nie udało się uruchomić serwera! Spróbuj ponownie.");
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
            test = false;
        }
        return test;
    }
    
    /**
     * Metoda wyłącza serwer
     */
    public void wylaczSerwer(){
        serwer.Stop();
        gui.setSerw(false);
        setTekst("Serwer został wyłączony!");
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor);        
    }
    
    /**
     * Metoda resetuje serwer
     * @param port port na którym ma zostać uruchomiony serwer
     */
    public void resetujSerwer(int port){      
        serwer.Stop();
        serwer = new Serwer(port, this);
        serwer.Start();
    }
    
    /**
     * Metoda łączy klienta z serwerem
     * @param adres adres serwera
     * @param port port na którym działa serwer
     * @return true jeśli udało się połaczyć z serwerm, w przeciwnym razie false
     */
    public boolean polaczKlienta(String adres, int port){
        boolean test;
        gui.setSerw(false);
        klient = new Klient(adres, port, this);
        if(klient.Start()){
            klient.nadaj("jestem");
            gui.setRozpocznijGre(true);
            gui.setCzatWyslij(true);
            setTekst("Połączenie zostało nawiązane! Kliknij \"Rozpocznij grę\" by przystąpić do rywalizacji.");
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);
            test = true;
        }
        else{
            klient = null;
            setTekst("Nie udało się nawiązać połączenia. Spróbuj ponownie!");
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);
            test = false;
        }
        return test;
    }
    
    /**
     * Metoda zrywa połaczenie klienta z serwerm
     */
    public void KlientZerwijPolaczenie(){
        klient.nadaj("zrywam");
        klient.Stop();
        gui.czyscChat();
        gui.setLosuj(true);
        gui.setRozpocznijGre(false);
        gui.setCzatWyslij(false);
        gui.getPoleRywala().zerowanieTablicy();
        setTekst("Połączenie zostało zerwane!");
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda zrywa połączenie serwera z klientem
     */
    public void SerwerZerwijPolaczenie(){
        serwer.nadaj("zrywam");
        serwer.Stop();
        serwer.setIsConnect(false);
        gui.czyscChat();
        gui.setLosuj(true);
        gui.setRozpocznijGre(false);
        gui.setCzatWyslij(false);
        gui.getPoleRywala().zerowanieTablicy();
        setTekst("Serwer został wyłączony!");
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda zgłasza zerwanie połaczenia serwerowi
     */
    public void zglosZerwanieSerwerowi(){
        resetujSerwer(getGui().getPortX());
        serwer.setIsSerwerStart(false);
        serwer.setIsKlientStart(false);
        serwer.setIsConnect(false);
        gui.czyscChat();
        gui.setLosuj(true);
        gui.setRozpocznijGre(false);
        gui.setKoniec1("Rozpocznij grę");
        gui.setCzatWyslij(false);
        gui.getPoleRywala().zerowanieTablicy();        
        setTekst("Połączenie zostało zerwane!");
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor); 
    }
    
    /**
     * Metoda zgłasza zerwanie połączenia klientowi
     */
    public void zglosZerwanieKlientowi(){
        gui.setPakiet();
        klient.setIsConnect(false);
        klient.setIsKlientStart(false);       
        gui.czyscChat();
        gui.setLosuj(true);
        gui.setRozpocznijGre(false);
        gui.setKoniec1("Rozpocznij grę");
        gui.setCzatWyslij(false);
        gui.getPoleRywala().zerowanieTablicy();        
        setTekst("Połączenie zostało zerwane!");
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor); 
    }
    
    /**
     * Metoda przerywa grę przez klienta
     */
    public void przerwijKlient(){
        gui.setToken(false);
        gui.setLosuj(true);
        klient.setIsKlientStart(false);
        klient.setIsSerwerStart(false);
        gui.setKoniec1("Rozpocznij grę");
        setTekst("Twój rywal się poddał! Odnosisz zwycięstwo.!"); 
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }

    /**
     * Metoda przerywa grę przez serwer
     */
    public void przerwijSerwer(){
        gui.setToken(false);
        gui.setLosuj(true);
        serwer.setIsKlientStart(false);
        serwer.setIsSerwerStart(false);
        gui.setKoniec1("Rozpocznij grę");
        setTekst("Twój rywal się poddał! Odnosisz zwycięstwo!"); 
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }
    
    /**
     * Metoda zwraca obiekt klasy Klient
     * @return obiekt klasy Klient
     */
    public Klient getKlient(){
        return klient;
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na złe samodzielne rozstawienie floty
     */
    public void zleUstawienie(){
        setTekst("Zaproponowane ustawienie floty jest niepoprawne! Spróbuj jeszcze raz zgodnie z zasadami:\nJeden Czteromasztowiecz (cztery kratki)\nDwa Trójmasztowce (trzy kratki)\nTrzy Dwumasztowce (dwie kratki)\n Cztery Jednomasztowce (jedna kratka)"); 
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda zgłasza gotowość do gry klienta
     */
    public void zglosGotowoscKlienta(){
        serwer.setIsConnect(true);
        gui.setRozpocznijGre(true);
        gui.setCzatWyslij(true);
        setTekst("Połączenie zostało nawiązane! Kliknij \"Rozpocznij grę\" by przystąpić do rywalizacji.");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na przerwanie gry
     */
    public void przerwanaGra(){
        getGui().setToken(false);
        setTekst("Poddałeś się! Twoja flota ponosi haniebną klęskę!"); 
        setColor(kolor = new Color(255, 144, 122));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na prawidłowe samodzielne rozstawienie floty
     */
    public void dobreUstawienie(){
        setTekst("Rozstawienie floty jest poprawne!");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }
    
    /**
     * Metoda wywołuje metodę dodającą do czata wiadomośc wysłaną przez przeciwnika
     * @param tekst treść wiadomości
     */
    public void setChat(String tekst){
        gui.setChat1(tekst);
    }
    
    /**
     * Metoda zgłasza rozpoczęcie gry przez klienta
     */
    public void klientZglosStart(){
        klient.nadaj("start");
        klientSetStart(true);
    }
    
    /**
     * Metoda zgłasza rozpoczęcie gry przez serwer
     */
    public void serwerZglosStart(){
        serwer.nadaj("start");
        serwerSetStart(true);
    }
    
    /**
     * Metoda zmienia stan gry pomiędzy start/stop po stronie klienta
     * @param test stan gry
     */
    public void klientSetStart(boolean test){
        klient.setIsKlientStart(test);
    }
    
    /**
     * Metoda zmienia stan gry pomiędzy start/stop po stronie serwera
     * @param test stan gry
     */    
    public void serwerSetStart(boolean test){
        serwer.setIsSerwerStart(test);
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na rozpoczęcie gry po stronie serwera
     */
    public void startGrySerwer(){
        setTekst("Gra rozpoczęta. Twój ruch!");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }

    /**
     * Metoda wywołuje komunikat w reakcji na rozpoczęcie gry po stronie klienta
     */    
    public void startGryKlient(){
        setTekst("Gra rozpoczęta. Zaczyna przeciwnik!");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);        
    }
    
    /**
     * Metoda wywołuje komunikaty po stronie gracza trafionego w reakcji na strzał rywala
     * @param tekst1 typ statku
     * @param stopien poziom uszkodzenia jednostki
     */
    public void komunikatTrafionego(String tekst1, int stopien){
        if(tekst1.equals("Jednomasztowiec") && stopien == 0){
            setTekst("Twój Jednomasztowiec został zatopiony\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Jednomasztowiec") && stopien == 1){
            setTekst("Admirale twój Jednomasztowiec idzie na dno!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }  
        else if(tekst1.equals("Jednomasztowiec") && stopien == 2){
            setTekst("Twój Jednomasztowiec został unicestwiony!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }        
        else if(tekst1.equals("Dwumasztowiec") && stopien == 2){
            setTekst("Rufa twojego Dwumasztowca została poważnie uszkodzona!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }        
        else if(tekst1.equals("Dwumasztowiec") && stopien == 1){
            setTekst("Admirale straciłeś Dwumasztowiec!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Trójmasztowiec") && stopien == 3){
            setTekst("Admirale woda w części dziobowej!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Trójmasztowiec") && stopien == 2){
            setTekst("Twój Trójmasztowiec został trafiony po raz drugi!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Trójmasztowiec") && stopien == 1){
            setTekst("Admirale twój Trójmasztowiec poszedł na dno!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }        
        else if(tekst1.equals("Czteromasztowiec") && stopien == 4){
            setTekst("Admirale okrężnica twojego Czteromasztowieca została uszkodzona!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Czteromasztowiec") && stopien == 3){
            setTekst("Twój Czteromasztowiec nabiera wody!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Czteromasztowiec") && stopien == 2){
            setTekst("Twój Czteromasztowiec ledwo trzyma się na wodzie!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }
        else if(tekst1.equals("Czteromasztowiec") && stopien == 1){
            setTekst("Admirale duma twojej floty idzie na dno!\nPrzeciwnik dalej atakuje!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);            
        }        
        else{
            setTekst("Przeciwnik pudłuje!\nTwój Ruch!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);             
        }        
    }
    
    /**
     * Metoda wywołuje komunikat po stronie gracza trafiającego w reakcji na strzał
     * @param zd typ statku
     * @param stopien poziom uszkodzenia jednostki 
     */
    public void komunikatTrafiajacego(int zd, int stopien){
        if(zd == 1 && stopien == 0){
            setTekst("Zatopiłeś Jednomasztowiec wroga!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 1 && stopien == 1){
            setTekst("Admirale Jednomasztowiec wroga idzie na dno!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }  
        else if(zd == 1 && stopien == 2){
            setTekst("Unicestwiłeś Jednomasztowiec wrogich sił!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }        
        else if(zd == 2 && stopien == 2){
            setTekst("Brawo! Trafiłeś rufę Dwumasztowca wroga!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }        
        else if(zd == 2 && stopien == 1){
            setTekst("Admirale zatopiłeś wrogi Dwumasztowiec!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 3 && stopien == 3){
            setTekst("Admirale udało ci się uszkodzić Trójmasztowiec wroga!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 3 && stopien == 2){
            setTekst("Po raz drugi trafiłeś wrogi Trójmasztowiec!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 3 && stopien == 1){
            setTekst("Admirale zatopiłeś Trójmasztowiec!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }        
        else if(zd == 4 && stopien == 4){
            setTekst("Admirale trafiłeś Czteromasztowiec wrogich sił!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 4 && stopien == 3){
            setTekst("Czteromasztowiec wroga nabiera wody!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 4 && stopien == 2){
            setTekst("Wrogi Czteromasztowiec ledwo dryfuje!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else if(zd == 4 && stopien == 1){
            setTekst("Brawo! Zatopiłeś dumę floty wroga!\nAtakuj dalej!"); 
            setColor(kolor = new Color(173, 246, 157));
            gui.setDialog(tekst, kolor);            
        }
        else {
            setTekst("Pudło!\nRuch przeciwnika!"); 
            setColor(kolor = new Color(255, 144, 122));
            gui.setDialog(tekst, kolor);             
        }
    }    
    
    /**
     * Metoda obsługuje zdarzenia w reakcji na porażkę
     */
    public void przegralem(){
        if(gui.getGraKomp()==false){
            if(gui.getSerw()){
                serwer.setIsKlientStart(false);
                serwer.setIsSerwerStart(false);
            }
            else{
                klient.setIsKlientStart(false);
                klient.setIsSerwerStart(false);
            }
        }
        gui.setKoniec1("Rozpocznij grę");
        setTekst("Admirale! Cała twoja flota została ztopiona. Bitwa przegrana!"); 
        setColor(kolor = new Color(255, 144, 122));
        gui.setLosuj(true);
        gui.setRozpocznijGre(true);
        gui.setToken(false);
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda obsługuje zdarzenia w reakcji na zwycięstwo
     */    
    public void wygralem(){
        if(gui.getGraKomp()==false){
            if(gui.getSerw()){
                serwer.setIsKlientStart(false);
                serwer.setIsSerwerStart(false);
            }
            else{
                klient.setIsKlientStart(false);
                klient.setIsSerwerStart(false);
            }
        }
        gui.setKoniec1("Rozpocznij grę");
        setTekst("Admirale! Wróg pokonany. Twoja flota odniosła wspaniałe zwycięstwo!");
        setColor(kolor = new Color(173, 246, 157));
        gui.setLosuj(true);
        gui.setRozpocznijGre(true);
        gui.setToken(false);
        gui.setDialog(tekst, kolor);     
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na gotowość do gry przeciwnika
     */
    public void gotowy(){
        setTekst("Twój rywal jest już gotowy! Kliknij \"Rozpocznij grę\" aby dołączyć do bitwy.");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda wywołuje komunikat w reakcji na oczekiwanie na przeciwnika
     */
    public void czekajacy(){
        setTekst("Trwa oczekiwanie na rywala!");
        setColor(kolor = new Color(173, 246, 157));
        gui.setDialog(tekst, kolor);         
    }
    
    /**
     * Metoda zwraca obiekt klasy GUI
     * @return obiekt klasy GUI
     */
    public GUI getGui(){
        return gui;
    }
    
    /**
     * Metoda zwraca obiekt klasy Serwer
     * @return obiekt klasy Serwer
     */
    public Serwer getSerwer(){
        return serwer;
    }

}
