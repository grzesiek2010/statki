package gra;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import siec.Zdarzenia;

/**
 * Klasa zawiera GUI programu
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */

public class GUI extends JFrame implements Runnable{

    /** Główny panel GUI */
    private JPanel panel;
    /** Pola tekstowe */
    private JLabel pole1, pole2, pole3, pole4, pole5, pole6, pole7, poleA, poleB, poleC, poleD, poleE, poleF, poleG, poleH, poleI, poleJ, pol1, pol2, pol3, pol4, pol5, pol6, pol7, pol8, pol9, pol10, poleAA, poleBB, poleCC, poleDD, poleEE, poleFF, poleGG, poleHH, poleII, poleJJ, pol11, pol22, pol33, pol44, pol55, pol66, pol77, pol88, pol99, pol1010;
    /** Przycisk ropoczynający grę */
    private JButton rozpocznijGre;
    /** Przycisk uruchamiający serwer */
    private JButton start;
    /** Przycisk łączący użytkownika z serwerem */
    private JButton polacz;
    /** Przycisk automatycznie rozstawiający flotę */
    private JButton losuj;
    /** Przycisk aktywujący tryb samodzielnego rozstawiania floty */
    private JButton ustaw;
    /** Przycisk radiowy umożliwiający przejście w tryb klienta */
    private JRadioButton klient;
    /** Przycisk radiowy umożliwiający przejście w tryb serwera */
    private JRadioButton serwer;
    /** Pole tekstowe służące do wprowadzania adresu serwera */
    private JTextField adres;
    /** Pole tekstowe służące do wprowadzania portu przez klienta */
    private JTextField port;
    /** Pole tekstowe służące do wprowadzania portu na którym ma działać serwer */
    private JTextField port2;
    /** Pole tektowe służące do wysyłania wiadomości na czacie */
    private JTextField czatWyslij;
    /** Obszar tekstowey służący do wyświetlania komunikatów */
    private JTextArea dialog;
    /** Obszar tekstowy służący do wyświetlania wiadomości czatu */
    private JTextArea czatOdbierz;
    /** Pasek menu */
    private JMenuBar menu;
    /** Menu "Plik" w pasku menu */
    private JMenu plik;
    /** Menu "Pomoc" w pasku menu */
    private JMenu pomoc;
    /**Podmenu tryb w menu "Plik" */
    private JMenu tryb;
    /** Element menu "Pomoc" */
    private JMenuItem oProgramie;
    /** Przycisk radiowy w pasku menu przenoszący użytkownika w tryb gry sieciowej */
    private JRadioButtonMenuItem graSieciowa;
    /** Przycisk radiowy w pasku menu przenoszący użytkownika w tryb gry z komputerem */
    private JRadioButtonMenuItem graKomputer;
    /** Panel przwijania komunikatów w oknie dialogowym */
    private JScrollPane suwak;
    /** Panel przwijania wiadomości w czacie */
    private JScrollPane suwak2;
    /** Zmienna reprezentująca kolor tła okna dialogowego */
    private Color color;
    /** Plansza gracza oraz przeciwnika */
    private PoleBitwy planszaGracza, planszaPrzeciwnika;
    /** Zmienna reprezentująca ilość zatopionych statków gracza */
    private int statkiGracza;
    /*Zmienna reprezentująca zatopione statki przciwnika */
    private int statkiPrzeciwnika;
    /** Obiekt klasy Zdarzenie */
    private Zdarzenia zdarzenie = new Zdarzenia(this);
    /** ZMienna oktreślająca czy gra działa w trybie serwera */
    private boolean serw = false;
    /** Zmienna określająca rozpoczęcie gry z komputerem */
    private boolean graKompStart = false;
    /** Zmienna służąca do wysyłania wiadomości na czacie */
    private String tekst;
    /** Zmienna wskazująca gracza do którego nalezy ruch */
    private boolean token;
    /** Zmienna określająca aktywnośc trybu gry z komputerem */
    private boolean graKomp = false;
    /** Zmienna określająca domyślny adres serwera */
    private String adr = "localhost";
    /** Zmienna określające domyślny port po przez który łączył będzie się klient */
    private int prt = 3542;
    /** Zmienna określająca domyślny port uruchamiania serwera */
    private int prt2 = 3542;
    /** Zmienna określająca stronę rozpoczynającą grę w trybie gry z komputerem */
    private int kolej;
    /** Zmienna służąca do losowania */
    private Random rand = new Random();
    /** Licznik kliknięć przycisku "Rozpocznij grę" */
    private int licznik = 0;
    /** Zmienna określająca aktywnośc trybu samodzielnego rozstawiania floty */
    private boolean rozstawFlote = false;
    /** Obiekt klasy Timer użyty do podświetlenia czata przy odbioerze wiadomości */
    public Timer timer;

    /**
     * Metoda uruchamiająca wątek z grą
     */
    public void Start(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI klasa = new GUI();
 		klasa.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                klasa.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent evt) {
                        formWindowClosing(evt);
                    }
                }); 
                klasa.setVisible(true);
                klasa.setSize(714, 634);
                klasa.setResizable(false);
                klasa.setContentPane(getJContentPane());
                klasa.setTitle("Gra w Statki");                      
                klasa.setJMenuBar(menu());
            }
	});
                Runnable r = new GUI();
                Thread t = new Thread(r);
                t.start();    
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        int ans = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyjść z gry?", "Zamknąć?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION) {
            // Zamknięcie okna
            if(serw && zdarzenie.getSerwer()!=null && zdarzenie.getSerwer().isConnect())
                zdarzenie.SerwerZerwijPolaczenie();
            else if(zdarzenie.getKlient()!=null && zdarzenie.getKlient().getIsConnect())
                zdarzenie.KlientZerwijPolaczenie();            
            System.exit(0);       
        }
    }    
    
    /**
     * Metoda zwracająca element GUI jakim jest główny panel okna
     * @return główny panel okna 
     */
    private JPanel getJContentPane() {
        panel = new JPanel();
        panel.setLayout(null);      
        panel.add(getPlanszaGracza());
        panel.add(getPlanszaPrzeciwnika());
        pole1 = new JLabel();
        pole1.setBounds(new Rectangle(12, 10, 117, 16));
        pole1.setText("Twoje statki:");
        pole1.setFont(new Font("Cooper Black", Font.BOLD, 12));
        panel.add(pole1);
        pole2 = new JLabel();
        pole2.setBounds(new Rectangle(170, 10, 117, 16));
        pole2.setText("zatopione:");
        pole2.setFont(new Font("Cooper Black", Font.BOLD, 12));
        panel.add(pole2);     
        pole3 = new JLabel();
        pole3.setBounds(new Rectangle(260, 10, 117, 16));
        pole3.setText(statkiGracza + "/10");
        pole3.setFont(new Font("Cooper Black", Font.BOLD, 20));
        panel.add(pole3);              
        pole4 = new JLabel();
        pole4.setBounds(new Rectangle(355, 10, 157, 16));
	pole4.setText("Statki przeciwnika:");
        pole4.setFont(new Font("Cooper Black", Font.BOLD, 12));
        panel.add(pole4);
        pole5 = new JLabel();
        pole5.setBounds(new Rectangle(522, 10, 117, 16));
        pole5.setText("zatopione:");
        pole5.setFont(new Font("Cooper Black", Font.BOLD, 12));
        panel.add(pole5);       
        pole6 = new JLabel();
        pole6.setBounds(new Rectangle(611, 10, 117, 16));
        pole6.setText(statkiPrzeciwnika + "/10");
        pole6.setFont(new Font("Cooper Black", Font.BOLD, 20));
        panel.add(pole6);
        poleA = new JLabel();
        poleA.setText("A");
        poleA.setBounds(new Rectangle(44, 35, 117, 16));
        poleA.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleA);
        poleB = new JLabel();
        poleB.setText("B");
        poleB.setBounds(new Rectangle(74, 35, 117, 16));
        poleB.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleB);        
        poleC = new JLabel();
        poleC.setText("C");
        poleC.setBounds(new Rectangle(104, 35, 117, 16));
        poleC.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleC);        
        poleD = new JLabel();
        poleD.setText("D");
        poleD.setBounds(new Rectangle(134, 35, 117, 16));
        poleD.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleD);        
        poleE = new JLabel();
        poleE.setText("E");
        poleE.setBounds(new Rectangle(164, 35, 117, 16));
        poleE.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleE);
        poleF = new JLabel();
        poleF.setText("F");
        poleF.setBounds(new Rectangle(194, 35, 117, 16));
        poleF.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleF);
        poleG = new JLabel();
        poleG.setText("G");
        poleG.setBounds(new Rectangle(224, 35, 117, 16));
        poleG.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleG);        
        poleH = new JLabel();
        poleH.setText("H");
        poleH.setBounds(new Rectangle(254, 35, 117, 16));
        poleH.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleH);        
        poleI = new JLabel();
        poleI.setText("I");
        poleI.setBounds(new Rectangle(284, 35, 117, 16));
        poleI.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleI);        
        poleJ = new JLabel();
        poleJ.setText("J");
        poleJ.setBounds(new Rectangle(314, 35, 117, 16));
        poleJ.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleJ);
        pol1 = new JLabel();
        pol1.setText("1");
        pol1.setBounds(new Rectangle(19, 61, 117, 16));
        pol1.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol1);
        pol2 = new JLabel();
        pol2.setText("2");
        pol2.setBounds(new Rectangle(19, 91, 117, 16));
        pol2.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol2);        
        pol3 = new JLabel();
        pol3.setText("3");
        pol3.setBounds(new Rectangle(19, 121, 117, 16));
        pol3.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol3);        
        pol4 = new JLabel();
        pol4.setText("4");
        pol4.setBounds(new Rectangle(19, 151, 117, 16));
        pol4.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol4);        
        pol5 = new JLabel();
        pol5.setText("5");
        pol5.setBounds(new Rectangle(19, 181, 117, 16));
        pol5.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol5);        
        pol6 = new JLabel();
        pol6.setText("6");
        pol6.setBounds(new Rectangle(19, 211, 117, 16));
        pol6.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol6);        
        pol7 = new JLabel();
        pol7.setText("7");
        pol7.setBounds(new Rectangle(19, 241, 117, 16));
        pol7.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol7);        
        pol8 = new JLabel();
        pol8.setText("8");
        pol8.setBounds(new Rectangle(19, 271, 117, 16));
        pol8.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol8);        
        pol9 = new JLabel();
        pol9.setText("9");
        pol9.setBounds(new Rectangle(19, 301, 117, 16));
        pol9.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol9);
        pol10 = new JLabel();
        pol10.setText("10");
        pol10.setBounds(new Rectangle(9, 331, 117, 16));
        pol10.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol10);        
        poleAA = new JLabel();
        poleAA.setText("A");
        poleAA.setBounds(new Rectangle(396, 35, 117, 16));
        poleAA.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleAA);        
        poleBB = new JLabel();
        poleBB.setText("B");
        poleBB.setBounds(new Rectangle(426, 35, 117, 16));
        poleBB.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleBB); 
        poleCC = new JLabel();
        poleCC.setText("C");
        poleCC.setBounds(new Rectangle(456, 35, 117, 16));
        poleCC.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleCC);         
        poleDD = new JLabel();
        poleDD.setText("D");
        poleDD.setBounds(new Rectangle(486, 35, 117, 16));
        poleDD.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleDD);         
        poleEE = new JLabel();
        poleEE.setText("E");
        poleEE.setBounds(new Rectangle(516, 35, 117, 16));
        poleEE.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleEE);         
        poleFF = new JLabel();
        poleFF.setText("F");
        poleFF.setBounds(new Rectangle(546, 35, 117, 16));
        poleFF.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleFF);         
        poleGG = new JLabel();
        poleGG.setText("G");
        poleGG.setBounds(new Rectangle(576, 35, 117, 16));
        poleGG.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleGG);         
        poleHH = new JLabel();
        poleHH.setText("H");
        poleHH.setBounds(new Rectangle(606, 35, 117, 16));
        poleHH.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleHH);         
        poleII = new JLabel();
        poleII.setText("I");
        poleII.setBounds(new Rectangle(636, 35, 117, 16));
        poleII.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleII); 
        poleJJ = new JLabel();
        poleJJ.setText("J");
        poleJJ.setBounds(new Rectangle(666, 35, 117, 16));
        poleJJ.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(poleJJ);         
        pol11 = new JLabel();
        pol11.setText("1");
        pol11.setBounds(new Rectangle(371, 61, 117, 16));
        pol11.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol11);
        pol22 = new JLabel();
        pol22.setText("2");
        pol22.setBounds(new Rectangle(371, 91, 117, 16));
        pol22.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol22);        
        pol33 = new JLabel();
        pol33.setText("3");
        pol33.setBounds(new Rectangle(371, 121, 117, 16));
        pol33.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol33);        
        pol44 = new JLabel();
        pol44.setText("4");
        pol44.setBounds(new Rectangle(371, 151, 117, 16));
        pol44.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol44);        
        pol55 = new JLabel();
        pol55.setText("5");
        pol55.setBounds(new Rectangle(371, 181, 117, 16));
        pol55.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol55);        
        pol66 = new JLabel();
        pol66.setText("6");
        pol66.setBounds(new Rectangle(371, 211, 117, 16));
        pol66.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol66);        
        pol77 = new JLabel();
        pol77.setText("7");
        pol77.setBounds(new Rectangle(371, 241, 117, 16));
        pol77.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol77);        
        pol88 = new JLabel();
        pol88.setText("8");
        pol88.setBounds(new Rectangle(371, 271, 117, 16));
        pol88.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol88);        
        pol99 = new JLabel();
        pol99.setText("9");
        pol99.setBounds(new Rectangle(371, 301, 117, 16));
        pol99.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol99);        
        pol1010 = new JLabel();
        pol1010.setText("10");
        pol1010.setBounds(new Rectangle(361, 331, 117, 16));
        pol1010.setFont(new Font("Arial Black", Font.BOLD, 13));
        panel.add(pol1010);               
        panel.add(getRozpocznijGre());
        panel.add(getUstawienie());
        panel.add(getUstaw());
        panel.add(getDialog());
        panel.add(getKlient());
        panel.add(getSerwer());
        ButtonGroup grupa = new ButtonGroup();
        grupa.add(klient);
	grupa.add(serwer);
        panel.add(getAdres());
        panel.add(getPort());
        panel.add(getPort2());
        panel.add(getPolacz());
        panel.add(getStart());
        pole7 = new JLabel();
        pole7.setBounds(new Rectangle(387, 379, 72, 16));
        pole7.setText("Czat:");
        pole7.setFont(new Font("Cooper Black", Font.BOLD, 20));
        panel.add(pole7);
        panel.add(getCzatWyslij());
        panel.add(getCzatOdbierz());
        adres.setVisible(false);
        port.setVisible(false);
        port2.setVisible(true);
	polacz.setVisible(false);
        return panel;
    }

    /**
     * Metoda zwracająca element GUI jakim jest pasek menu
     * @return pasek menu
     */
    private JMenuBar menu(){
        menu = new JMenuBar();
        plik = new JMenu("Plik");
        tryb = new JMenu("Tryb Gry");
        ButtonGroup grupa = new ButtonGroup();
        graSieciowa = new JRadioButtonMenuItem("Gra przez sieć");
        graKomputer = new JRadioButtonMenuItem("Gra z komputerem");
        graSieciowa.setSelected(true);
        grupa.add(graSieciowa);
        grupa.add(graKomputer);
        graKomputer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rozpocznijGre.setEnabled(true);
                klient.setVisible(false);
                serwer.setVisible(false);
                port2.setVisible(false);
                start.setVisible(false);
                adres.setVisible(false);
                port.setVisible(false);
                polacz.setVisible(false);
                pole7.setVisible(false);
                czatWyslij.setVisible(false);
                czatOdbierz.setVisible(false);
                suwak2.setVisible(false);
                graKomp = true;
            }
	});
        graSieciowa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                klient.setVisible(true);
                serwer.setVisible(true);
                pole7.setVisible(true);
                czatWyslij.setVisible(true);
                czatOdbierz.setVisible(true);
                suwak2.setVisible(true);
                graKomp = false;
                if(serwer.isSelected()){
                    port2.setVisible(true);
                    start.setVisible(true);
                }
                else{
                    adres.setVisible(true);
                    port.setVisible(true);
                    polacz.setVisible(true);
                }
            }
	});        
        tryb.add(graSieciowa);
        tryb.add(graKomputer);
        plik.add(tryb);   
        pomoc = new JMenu("Pomoc");
        oProgramie = new JMenuItem("O programie", 'O');
        oProgramie.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        pomoc.add(oProgramie);
        menu.add(plik);
        menu.add(pomoc);        
        oProgramie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "<html><center><h1><i>Gra w Statki</i></h1><hr>Autor: Grzegorz Orczykowski<BR/>e-mail: <a href=\"mailto:grzesiek@mat.umk.pl\">grzesiek@mat.umk.pl</a><BR/>wersja: 1.0<BR/>data wydania: 20.08.2012<BR/>Praca zaliczeniowa z przedmiotu Programowanie II Java<BR/>II NSI Uniwersytetu Mikołaja Kopernika</center></html>", "O programie", JOptionPane.INFORMATION_MESSAGE);
            }
	});
        
        return menu;
    }
   
    private PoleBitwy getPlanszaGracza() {
        planszaGracza = new PoleBitwy(Gracze.GRACZ, this);
	planszaGracza.setLocation(new java.awt.Point(33, 54));
	return planszaGracza;
    }

    private PoleBitwy getPlanszaPrzeciwnika() {
	planszaPrzeciwnika = new PoleBitwy(Gracze.PRZECIWNIK, this);
	planszaPrzeciwnika.setLocation(new java.awt.Point(385, 54));
        return planszaPrzeciwnika;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk rozpoczęcia gry
     * @return przycisk rozpoczynający grę
     */
    private JButton getRozpocznijGre() {
	rozpocznijGre = new JButton();
	rozpocznijGre.setLocation(new Point(40, 371));
        rozpocznijGre.setEnabled(false);
	rozpocznijGre.setText("Rozpocznij grę");
	rozpocznijGre.setSize(new Dimension(131, 23));
        rozpocznijGre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                losuj.setEnabled(false);
                ustaw.setEnabled(false);
                if(graKomp == false){
                    if(serw){
                        if(!zdarzenie.getSerwer().getIsSerwerStart()){
                            if(licznik!=0){
                                statkiGracza = 0;         
                                statkiPrzeciwnika = 0;
                                pole3.setText(statkiGracza + "/10");
                                pole6.setText(statkiPrzeciwnika + "/10");                                
                                planszaPrzeciwnika.zerowanieTablicy();
                                for(int i=0; i<10; i++)
                                    for(int j=0; j<10; j++)
                                        if(planszaGracza.getTablica()[i][j]==StatusPola.PUDLO || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_TRAFIONY || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_ZATOPIONY)
                                            planszaGracza.rozstaw();   
                            }          
                            licznik++;                            
                            token = true;
                            rozpocznijGre.setText("Przerwij grę");
                            zdarzenie.serwerZglosStart();
                            if(zdarzenie.getSerwer().getIsKlientStart() == false)
                                zdarzenie.czekajacy();
                            if(zdarzenie.getSerwer().getIsKlientStart() == true && zdarzenie.getSerwer().getIsSerwerStart() == true)
                                zdarzenie.startGrySerwer();
                        }
                        else{
                            rozpocznijGre.setText("Rozpocznij grę");
                            setLosuj(true);
                            zdarzenie.getSerwer().setIsSerwerStart(false);
                            if(zdarzenie.getSerwer().getIsKlientStart()){
                                zdarzenie.przerwanaGra();
                                zdarzenie.getSerwer().nadaj("przerywam");
                                zdarzenie.getSerwer().setIsKlientStart(false);
                            }
                        }
                    }
                    else{
                        if(!zdarzenie.getKlient().getIsKlientStart()){
                            if(licznik!=0){
                                statkiGracza = 0; 
                                statkiPrzeciwnika = 0;
                                pole3.setText(statkiGracza + "/10");
                                pole6.setText(statkiPrzeciwnika + "/10");                                
                                planszaPrzeciwnika.zerowanieTablicy();
                                for(int i=0; i<10; i++)
                                    for(int j=0; j<10; j++)
                                        if(planszaGracza.getTablica()[i][j]==StatusPola.PUDLO || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_TRAFIONY || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_ZATOPIONY)
                                            planszaGracza.rozstaw();   
                            }          
                            licznik++;                            
                            token = false;
                            rozpocznijGre.setText("Przerwij grę");
                            zdarzenie.klientZglosStart();
                            if(zdarzenie.getKlient().getIsSerwerStart() == false)
                                zdarzenie.czekajacy();                            
                            if(zdarzenie.getKlient().getIsKlientStart() == true && zdarzenie.getKlient().getIsSerwerStart() == true)
                                zdarzenie.startGryKlient();                 
                        }
                        else{
                            rozpocznijGre.setText("Rozpocznij grę");
                            losuj.setEnabled(true);
                            zdarzenie.getKlient().setIsKlientStart(false);
                            if(zdarzenie.getKlient().getIsSerwerStart()){
                                zdarzenie.przerwanaGra();
                                zdarzenie.getKlient().nadaj("przerywam");
                                zdarzenie.getKlient().setIsSerwerStart(false);
                            }
                        }
                    }
                }
                else{
                    if(graKompStart == false){
                        if(licznik!=0){
                            statkiGracza = 0;
                            statkiPrzeciwnika = 0;
                            pole3.setText(statkiGracza + "/10");
                            pole6.setText(statkiPrzeciwnika + "/10");
                            planszaPrzeciwnika.zerowanieTablicy();
                            for(int i=0; i<10; i++)
                                for(int j=0; j<10; j++)
                                    if(planszaGracza.getTablica()[i][j]==StatusPola.PUDLO || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_TRAFIONY || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_ZATOPIONY)
                                        planszaGracza.rozstaw();   
                        }          
                        licznik++;                        
                        rozpocznijGre.setText("Przerwij grę");
                        graKompStart = true;
                        planszaGracza.rozstawKomputer();
                        kolej = rand.nextInt(2);
                        if(kolej == 0){
                            dialog.setText("Rozpoczyna gracz!");
                            dialog.setBackground(color);
                            setToken(true);
                        }
                        else{
                            dialog.setText("Rozpoczyna komputer!");
                            dialog.setBackground(color);
                            setToken(false);
                            planszaGracza.strzalKomputer();
                        }
                    }
                    else{
                        losuj.setEnabled(true);
                        ustaw.setEnabled(true);
                        rozpocznijGre.setText("Rozpocznij grę");
                        graKompStart = false;
                        zdarzenie.przerwanaGra();
                    }
                }
            }
	});
        rozpocznijGre.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(licznik!=0){
                    statkiGracza = 0;
                    statkiPrzeciwnika = 0; 
                    pole3.setText(statkiGracza + "/10");
                    pole6.setText(statkiPrzeciwnika + "/10");
                    planszaPrzeciwnika.zerowanieTablicy();
                    for(int i=0; i<10; i++)
                        for(int j=0; j<10; j++)
                            if(planszaGracza.getTablica()[i][j]==StatusPola.PUDLO || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_TRAFIONY || planszaGracza.getTablica()[i][j]==StatusPola.STATEK_ZATOPIONY)
                                planszaGracza.rozstaw();   
                }          
                licznik++;
                losuj.setEnabled(false);
                ustaw.setEnabled(false);
                if(graKomp == false){
                    if(serw){
                        if(!zdarzenie.getSerwer().getIsSerwerStart()){
                            token = true;
                            rozpocznijGre.setText("Przerwij grę");
                            zdarzenie.serwerZglosStart();
                            if(zdarzenie.getSerwer().getIsKlientStart() == false)
                                zdarzenie.czekajacy();                            
                            if(zdarzenie.getSerwer().getIsKlientStart() == true && zdarzenie.getSerwer().getIsSerwerStart() == true)
                                zdarzenie.startGrySerwer();
                        }
                        else{
                            rozpocznijGre.setText("Rozpocznij grę");
                            losuj.setEnabled(true);
                            zdarzenie.getSerwer().setIsSerwerStart(false);
                            if(zdarzenie.getSerwer().getIsKlientStart()){
                                zdarzenie.przerwanaGra();
                                zdarzenie.getSerwer().nadaj("przerywam");
                                zdarzenie.getSerwer().setIsKlientStart(false);
                            }
                        }
                    }
                    else{
                        if(!zdarzenie.getKlient().getIsKlientStart()){
                            token = false;
                            rozpocznijGre.setText("Przerwij grę");
                            zdarzenie.klientZglosStart();
                            if(zdarzenie.getKlient().getIsSerwerStart() == false)
                                zdarzenie.czekajacy();                            
                            if(zdarzenie.getKlient().getIsKlientStart() == true && zdarzenie.getKlient().getIsSerwerStart() == true)
                                zdarzenie.startGryKlient();                 
                        }
                        else{
                            rozpocznijGre.setText("Rozpocznij grę");
                            losuj.setEnabled(true);
                            zdarzenie.getKlient().setIsKlientStart(false);
                            if(zdarzenie.getKlient().getIsSerwerStart()){
                                zdarzenie.przerwanaGra();
                                zdarzenie.getKlient().nadaj("przerywam");
                                zdarzenie.getKlient().setIsSerwerStart(false);
                            }
                        }
                    }
                }
                else{
                    if(graKompStart == false){
                        rozpocznijGre.setText("Przerwij grę");
                        graKompStart = true;
                        planszaGracza.rozstawKomputer();
                        kolej = rand.nextInt(2);
                        if(kolej == 0){
                            dialog.setText("Rozpoczyna gracz!");
                            dialog.setBackground(color);
                            setToken(true);
                        }
                        else{
                            dialog.setText("Rozpoczyna komputer!");
                            dialog.setBackground(color);
                            setToken(false);
                            planszaGracza.strzalKomputer();
                        }
                    }
                    else{
                        losuj.setEnabled(true);
                        ustaw.setEnabled(true);
                        rozpocznijGre.setText("Rozpocznij grę");
                        graKompStart = false;
                        zdarzenie.przerwanaGra();
                    }
                }
                }
            }
            public void keyReleased(KeyEvent e) {}
	});          
	return rozpocznijGre;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk służący do losowania rozstawienia floty
     * @return przycisk losujący rozstawienie floty
     */
    private JButton getUstawienie() {
	losuj = new JButton();
	losuj.setBounds(new Rectangle(176, 371, 160, 23));
	losuj.setText("Losuj ustawienie");
        losuj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                planszaGracza.rozstaw();
                setRozpocznijGre(true);
                ustaw.setText("Rozstaw flotę");
                rozstawFlote = false;
            }
	});
        losuj.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    planszaGracza.rozstaw();
                    setRozpocznijGre(true);
                    ustaw.setText("Rozstaw flotę");
                    rozstawFlote = false;
                }
            }
            public void keyReleased(KeyEvent e) {}
	});                
	return losuj;
    }

    /**
     * Metoda zwraca element GUI jakim jest przycisk służący do samodzielnego rozstawienia floty
     * @return przycisk samodzielnego rozstawienia floty
     */
    private JButton getUstaw() {
	ustaw = new JButton();
	ustaw.setBounds(new Rectangle(105, 405, 160, 23));
	ustaw.setText("Rozstaw flotę");
        ustaw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(rozstawFlote && planszaGracza.sprawdzUstwienie()){
                    ustaw.setText("Rozstaw flotę");
                    rozstawFlote = false;
                }
                else if(!rozstawFlote){
                    ustaw.setText("Akceptuj roszadę");
                    rozpocznijGre.setEnabled(false);
                    planszaGracza.zerowanieTablicy();
                    rozstawFlote = true;
                }
            }
	});
        ustaw.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(rozstawFlote && planszaGracza.sprawdzUstwienie()){
                        ustaw.setText("Rozstaw flotę");
                        rozstawFlote = false;
                    }
                    else if(!rozstawFlote){
                        ustaw.setText("Akceptuj roszadę");
                        rozpocznijGre.setEnabled(false);
                        planszaGracza.zerowanieTablicy();
                        rozstawFlote = true;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
	});                
	return ustaw;
    }    
    
    /**
     * Metoda zwraca element GUI jakim jest suwak służący do przewijania treści komunikatu przekazywanego przez komputer graczowi
     * @return suwak przewijania
     */
    private JScrollPane getDialog() {
        color = zdarzenie.getKolor();
	dialog = new JTextArea();
	dialog.setEnabled(true);
	dialog.setEditable(false);
	dialog.setLineWrap(true);
	dialog.setWrapStyleWord(true);
	dialog.setFont(new Font("Dialog", Font.BOLD, 12));
        dialog.setText(zdarzenie.getTekst());
        dialog.setBackground(color);
        suwak = new JScrollPane();
        suwak.setLocation(new Point(40, 440));
	suwak.setEnabled(true);
	suwak.setViewportView(dialog);
	suwak.setSize(new Dimension(297, 70));
	return suwak;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk wyboru trybu klienta
     * @return przycisk wyboru trybu klienta
     */ 
    private JRadioButton getKlient() {
	klient = new JRadioButton();
	klient.setLocation(new Point(40, 514));
	klient.setText("Klient");
	klient.setSize(new Dimension(71, 21));
	klient.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			start.setVisible(false);
			polacz.setVisible(true);
			adres.setVisible(true);
                        port.setVisible(true);
                        port2.setVisible(false);
		}
	});
	return klient;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk wyboru trybu serwera
     * @return przycisk wyboru trybu serwera
     */
    private JRadioButton getSerwer() {
	serwer = new JRadioButton();
	serwer.setLocation(new Point(40, 535));
	serwer.setSelected(true);
	serwer.setText("Serwer");
	serwer.setSize(new Dimension(72, 21));
	serwer.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			start.setVisible(true);
			polacz.setVisible(false);
			adres.setVisible(false);
                        port.setVisible(false);
                        port2.setVisible(true);
		}
	});
	return serwer;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk start służący do uruchomienia serwera
     * @return przycisk start
     */
    private JButton getStart() {
	start = new JButton();
	start.setBounds(new Rectangle(170, 522, 75, 26));
	start.setText("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(zdarzenie.getSerwer() == null || zdarzenie.getSerwer().isRunning() == false){
                    if(!port2.getText().equals("") && !port2.getText().equals("port"))
                        prt2 = Integer.parseInt(port2.getText().trim());
                    if(zdarzenie.uruchomSerwer(prt2)){
                        start.setText("Stop");
                        klient.setEnabled(false);
                        serwer.setEnabled(false);
                        port2.setEnabled(false);
                    }
                }
                else{
                    if(zdarzenie.getSerwer().isConnect() == true)
                        zdarzenie.SerwerZerwijPolaczenie();
                    else
                        zdarzenie.wylaczSerwer();
                    start.setText("Start");
                    rozpocznijGre.setText("Rozpocznij grę");
                    klient.setEnabled(true);
                    serwer.setEnabled(true);
                    port2.setEnabled(true);
                }
           }
	});
        start.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(zdarzenie.getSerwer() == null || zdarzenie.getSerwer().isRunning() == false){
                        if(!port2.getText().equals("") && !port2.getText().equals("port"))
                            prt2 = Integer.parseInt(port2.getText().trim());
                        if(zdarzenie.uruchomSerwer(prt2)){
                            start.setText("Stop");
                            klient.setEnabled(false);
                            serwer.setEnabled(false);
                            port2.setEnabled(false);
                        }
                    }
                    else{
                        if(zdarzenie.getSerwer().isConnect() == true)
                            zdarzenie.SerwerZerwijPolaczenie();
                        else
                            zdarzenie.wylaczSerwer();
                        start.setText("Start");
                        klient.setEnabled(true);
                        serwer.setEnabled(true);
                        port2.setEnabled(true);
                    }                    
                }
            }
            public void keyReleased(KeyEvent e) {}
	});        
        return start;
    }  
    
    /**
     * Metoda zwraca element GUI jakim jest pole służące do wpisania adresu serwera
     * @return pole adresu serwera
     */
    private JTextField getAdres() {
        adres = new JTextField();
	adres.setLocation(new Point(115, 528));
	adres.setSize(new Dimension(85, 20));
	adres.setText("adres");
        adres.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                adres.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(adres.getText().equals(""))
                    adres.setText("adres");
            }
	});        
	adres.setVisible(true);
        return adres;
    }
    
    /**
     * Metoda zwraca elemen GUI jakim jest pole służące do podania portu do połączenia z serwerem
     * @return pole do wpisywania portu serwera
     */
    private JTextField getPort() {
        port = new JTextField();
	port.setLocation(new Point(205, 528));
	port.setSize(new Dimension(40, 20));
        port.setVisible(true);
	port.setText("port");
        port.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                port.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(port.getText().equals(""))
                    port.setText("port");
            }
	});    
        port.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(zdarzenie.getKlient() == null || zdarzenie.getKlient().getIsConnect() == false){
                        if(!port.getText().equals("") && !port.getText().equals("port"))
                            prt = Integer.parseInt(port.getText().trim());
                        if(!adres.getText().equals("") && !adres.getText().equals("adres"))
                            adr = adres.getText().trim();
                        if(zdarzenie.polaczKlienta(adr, prt)){
                            klient.setEnabled(false);
                            serwer.setEnabled(false);
                            adres.setEnabled(false);
                            port.setEnabled(false);
                            polacz.setText("Rozłącz");
                        }
                    }
                    else{
                        klient.setEnabled(true);
                        serwer.setEnabled(true);
                        adres.setEnabled(true);
                        port.setEnabled(true);
                        polacz.setText("Połącz");
                        zdarzenie.KlientZerwijPolaczenie();
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
	});        
        return port;
    }
    
    /**
     * Metoda zwraca elemen GUI jakim jest pole służące do podania portu na jakim będzie działał serwer
     * @return pole do wpisywania portu serwera
     */
    private JTextField getPort2() {
        port2 = new JTextField();
	port2.setLocation(new Point(115, 528));
	port2.setSize(new Dimension(40, 20));
        port2.setVisible(true);
	port2.setText("port");
        port2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                port2.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(port2.getText().equals(""))
                    port2.setText("port");
            }
	});
        port2.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(zdarzenie.getSerwer() == null || zdarzenie.getSerwer().isRunning() == false){
                        if(!port2.getText().equals("") && !port2.getText().equals("port"))
                            prt2 = Integer.parseInt(port2.getText().trim());
                        if(zdarzenie.uruchomSerwer(prt2)){
                            start.setText("Stop");
                            klient.setEnabled(false);
                            serwer.setEnabled(false);
                            port2.setEnabled(false);
                        }
                    }
                    else{
                        if(zdarzenie.getSerwer().isConnect() == true)
                            zdarzenie.SerwerZerwijPolaczenie();
                        else
                            zdarzenie.wylaczSerwer();
                        start.setText("Start");
                        klient.setEnabled(true);
                        serwer.setEnabled(true);
                        port2.setEnabled(true);
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
	});
        return port2;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest przycisk polacz służący do łączenia z serwerem
     * @return przycisk polacz
     */
    private JButton getPolacz() {
        polacz = new JButton();
	polacz.setBounds(new Rectangle(250, 525, 86, 23));
	polacz.setText("Połącz");
	polacz.setVisible(true);
        polacz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(zdarzenie.getKlient() == null || zdarzenie.getKlient().getIsConnect() == false){
                    if(!port.getText().equals("") && !port.getText().equals("port"))
                        prt = Integer.parseInt(port.getText().trim());
                    if(!adres.getText().equals("") && !adres.getText().equals("adres"))
                        adr = adres.getText().trim();
                    if(zdarzenie.polaczKlienta(adr, prt)){
                        klient.setEnabled(false);
                        serwer.setEnabled(false);
                        adres.setEnabled(false);
                        port.setEnabled(false);
                        polacz.setText("Rozłącz");
                    }
                }
                
                else{
                    klient.setEnabled(true);
                    serwer.setEnabled(true);
                    adres.setEnabled(true);
                    port.setEnabled(true);
                    rozpocznijGre.setText("Rozpocznij grę");
                    polacz.setText("Połącz");
                    zdarzenie.KlientZerwijPolaczenie();
                }
            }
	});
        polacz.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if(zdarzenie.getKlient() == null || zdarzenie.getKlient().getIsConnect() == false){
                        if(!port.getText().equals("") && !port.getText().equals("port"))
                            prt = Integer.parseInt(port.getText().trim());
                        if(!adres.getText().equals("") && !adres.getText().equals("adres"))
                            adr = adres.getText().trim();
                        if(zdarzenie.polaczKlienta(adr, prt)){
                            klient.setEnabled(false);
                            serwer.setEnabled(false);
                            adres.setEnabled(false);
                            port.setEnabled(false);
                            polacz.setText("Rozłącz");
                        }
                    }
                    else{
                        klient.setEnabled(true);
                        serwer.setEnabled(true);
                        adres.setEnabled(true);
                        port.setEnabled(true);
                        polacz.setText("Połącz");
                        zdarzenie.KlientZerwijPolaczenie();
                    }
                }                   
            }
            public void keyReleased(KeyEvent e) {}
	});        
	return polacz;
    }
    
    private JTextField getCzatWyslij() {
	czatWyslij = new JTextField();
	czatWyslij.setLocation(new Point(386, 406));
	czatWyslij.setEnabled(false);
	czatWyslij.setSize(new Dimension(300, 20));
	czatWyslij.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    tekst = czatWyslij.getText().trim();
                    czatWyslij.setText("");
                    if(serw == true){
                        zdarzenie.getSerwer().nadaj("#$%" + tekst);
                        setChat2(tekst);
                    }
                    else{
                        zdarzenie.getKlient().nadaj("#$%" + tekst);
                        setChat2(tekst);
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
	});        
	return czatWyslij;
    }
    
    /**
     * Metoda zwraca element GUI jakim jest pole czatu służące do wysyłania wiadomości
     * @return panel wysyłania wiadomości czatu
     */
    private JScrollPane getCzatOdbierz() {
	czatOdbierz = new JTextArea();
	czatOdbierz.setEnabled(true);
	czatOdbierz.setLineWrap(true);
	czatOdbierz.setWrapStyleWord(true);
	czatOdbierz.setEditable(false);
        suwak2 = new JScrollPane();
        suwak2.setLocation(new Point(386, 432));
	suwak2.setViewportView(czatOdbierz);
	suwak2.setSize(new Dimension(300, 83));
	return suwak2;
    }
    
    /**
     * Metoda zmienia treść oraz tło elementu jakim jest okno kumunikacji komputera z graczem
     * @param tekst treść komunikatu przekazywanego graczowi
     * @param kolor kolor jaki ma przyjąć tło okna
     */
    public void setDialog(String tekst, Color kolor){
        dialog.setText(tekst);
        dialog.setBackground(kolor);
    }
    
    /**
     * Metoda zmienia aktywność przycisku rozpocznijGre
     * @param test wartość stanowiąca o aktywności lub deaktywności przycisku rozpocznijGre
     */
    public void setRozpocznijGre(boolean test){
        rozpocznijGre.setEnabled(test);
    }  
    
    /**
     * Metoda zmienia treśc przycisku rozpocznijGre oraz wartość pola graKompStart
     * @param tekst treść jaką ma przyjąć przycisk rozpocznijGre
     */
    public void setKoniec1(String tekst){
        rozpocznijGre.setText(tekst);
    }  
    
    /**
     * Metoda zmienia treśc przycisku rozpocznijGre oraz wartość pola graKompStart
     * @param tekst treść jaką ma przyjąć przycisk rozpocznijGre
     */
    public void setKoniec(String tekst){
        rozpocznijGre.setText(tekst);
        graKompStart = false;
    }
    
    /**
     * Metoda wualnia elementy związane z grą sieciową
     */
    public void setPakiet(){
        klient.setEnabled(true);
        serwer.setEnabled(true);
        adres.setEnabled(true);
        port.setEnabled(true);
        polacz.setText("Połącz");         
    }
    
    /**
     * Metoda zmienia dostępność pola czatWyslij
     * @param test stanowi o aktywnośći lubdeaktywnośći pola czatWyslij
     */
    public void setCzatWyslij(boolean test){
        czatWyslij.setEnabled(test);
    }
    
    /**
     * Metoda zmienia wartość pola serw
     * @param test stanowi o tym, czy serwer jest uruchomiony czy nie
     */
    public void setSerw(boolean test){
        serw = test;
    }

    /**
    * Metoda dodaje wiadomość wysłaną przez przeciwnika do pola czatu
    * @param tekstt treść wiadomości wysłanej przez przeciwnika
    */
    public void setChat1(String tekstt){
        tekst = czatOdbierz.getText();
        czatOdbierz.setText(tekst + "On: " + tekstt + "\n");
        czatOdbierz.setBackground(new Color(255,250,205));
        timer = new Timer();
        timer.schedule(new RemindTask(), 2000);
    }

    /**
    * Metoda dodaje wiadomość wysłaną przez gracza do pola czatu
    * @param tekstt treść wiadomości wysłanej przez gracza
    */
    public void setChat2(String tekstt){
        tekst = czatOdbierz.getText();
        czatOdbierz.setText(tekst + "Ty: " + tekstt + "\n");
    }
    
    /**
     * Metoda czyści zawartość pola czatu
     */
    public void czyscChat(){
        czatOdbierz.setText("");
    }
    
    /**
     * Metoda zmienia aktywność przycisków losuj oraz ustaw
     * @param test stanowi o aktywności lub deaktywności przycisków
     */
    public void setLosuj(boolean test){
        losuj.setEnabled(test);
        ustaw.setEnabled(test);
    }
    
    /**
     * Metoda zmienia wartość pola token
     * @param test stanowi o tym do kogo należy ruch
     */
    public void setToken(boolean test){
        token = test;
    }
    
    /**
     * Metoda zwrca wartość pola token
     * @return  true jeśli ruch należy do gracza, w przeciwnym razie false
     */
    public boolean getToken(){
        return token;
    }
    
    /**
     * Metoda zwraca obiekt zdarzenie
     * @return zdarzenie
     */
    public Zdarzenia getZdarzenie(){
        return zdarzenie;
    }
    
    /**
     * Metoda zwraca wartość pola serw
     * @return  true jeśli uruchomiliśmy grę jako serwer, w przeciwnym razie false
     */
    public boolean getSerw(){
        return serw;
    }
    
    /**
     * Metoda zwraca planszę gracza
     * @return plansza gracza
     */
    public PoleBitwy getPoleGracza(){
        return planszaGracza;
    }

    /**
     * Metoda zwraca planszę przeciwnika
     * @return plansza przeciwnika
     */
    public PoleBitwy getPoleRywala(){
        return planszaPrzeciwnika;
    }
    
    /**
     * Metoda zwiększa licznik zatopionych statków gracza
     */
    public void setStatkiGracza(){
        statkiGracza++;
        pole3.setText(statkiGracza + "/10");
        pole6.setText(statkiPrzeciwnika + "/10");
    }
    
    /**
     * Metoda zwiększa licznik zatopionych statków przeciwnika
     */
    public void setStatkiPrzeciwnika(){
        statkiPrzeciwnika++;
        pole3.setText(statkiGracza + "/10");
        pole6.setText(statkiPrzeciwnika + "/10");
    }
    
    /**
     * Metoda zwraca ilość zatopionych statkó gracza
     * @return  ilość zatopionych statków
     */
    public int getStatkiGracza(){
        return statkiGracza;
    }
    
    /**
     * Metoda zwraca ilość zatopionych statków przeciwnika
     * @return ilość zatopionych statków
     */
    public int getStatkiPrzeciwnika(){
        return statkiPrzeciwnika;
    }
    
    /**
     * Metoda zwraca numer portu z którego korzysta serwer
     * @return  numer portu
     */
    public int getPortX(){
        return Integer.parseInt(port2.getText().trim());
    }
    
    /**
     * Metoda zwraca wartość pola graKomp
     * @return  true jeśli jesteśmy w trybie gry z komputerem, w przeciwnym razie false
     */
    public boolean getGraKomp(){
        return graKomp;
    }
    
    /**
     * Metoda zwraca wartość pola graKomputerStart
     * @return  true jeśli rozpoczeliśmy grę z komputerem, w przeciwnym razie false 
     */
    public boolean getGraKompStart(){
        return graKompStart;
    }
    
    /**
     * Metoda zwraca wartość pola rozstawFlote
     * @return  true jeśli zmienna rozstawFlote ma wartość true, w przeciwnym razie false
     */
    public boolean getRozstawFlote(){
        return rozstawFlote;
    }

    class RemindTask extends TimerTask {
        public void run() {
            czatOdbierz.setBackground(new Color(255, 255, 255));
        }
    }    
    
    public void run() {
    }
}