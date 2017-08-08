package gra;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Klasa, której obiekty reprezentują plansze gry
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */

public class PoleBitwy extends JPanel{    
    /** Tablica odwzorowująca plansze gracza */
    private StatusPola[][] tablica = new StatusPola[10][10];
    /** Tablica odwzorowująca plansze komputera */
    private StatusPola[][] tablicaKomputer = new StatusPola[10][10];
    /** Tablica odwzorowująca planszę gracza widzianą przez komputer */
    private StatusPola[][] tablicaKomputer2 = new StatusPola[10][10];
    /** Zmienna określająca właściciela planszy */
    private Gracze wlasciciel; 
    /** lista przechowująca dane o statkach gracza */
    private ArrayList<Jednostka> okrety = new ArrayList<Jednostka>();
    /** lista przechowująca dane o statkach gracza */
    private ArrayList<Jednostka> okrety2 = new ArrayList<Jednostka>();
    /** lista przechowująca dane o statkach komputera */
    private ArrayList<Jednostka> okretyKomputer = new ArrayList<Jednostka>();
    /** lista przechowująca dane o statkach komputera */
    private ArrayList<Jednostka> okrety2Komputer = new ArrayList<Jednostka>();
    /** lista przechowująca możliwe strzały komputera */
    private ArrayList<Wspolrzedne> listKomp = new ArrayList<Wspolrzedne>();
    /** lista przechowująca zawężoną listę strzałów komputera */
    private ArrayList<Wspolrzedne> listKomp2 = new ArrayList<Wspolrzedne>();
    /** Obiekt klasy GUI */
    private GUI gui;
    /** Zmienna zawierająca rozmiarstatku */
    private int rozmiar;
    /** Zmienna mówiąca czy poprzedni strzał komputera był trafionym */
    private boolean traf = false;
    /** Zmienna przechowująca nazwę statku */
    private String nazwa;
    /** Obiekt klasy Timer użyty do symulacji myślenia komputera */
    public Timer timer;

    /**
     * Metoda zerująca tablicę odwzorowującą planszę gracza
     */
    public void zerowanieTablicy() {
	for (int i = 0; i < tablica.length; i++) {
		for (int j = 0; j < tablica[i].length; j++) {
			tablica[i][j] = StatusPola.POLE_PUSTE;
		}
	}
        repaint();
    }
    
    /**
     * Metoda automatycznie rozstawiająca statki na planszy gracza
     */
    public void rozstaw(){
        int x,y,d,l=3,s=0;
        boolean test = false;
        Wspolrzedne punkt = new Wspolrzedne(0,0);
        ArrayList<Wspolrzedne> listaXY = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> listaXY2 = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> lista = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> lista2 = new ArrayList<Wspolrzedne>();
        zerowanieTablicy();
        okrety.clear();
        okrety2.clear();
        Random randomGenerator = new Random();
        while(s<10){
            //Wybor pierwszego pola statku
            if(s==0){            
                x = randomGenerator.nextInt(10);
                y = randomGenerator.nextInt(10);
                tablica[x][y] = StatusPola.STATEK;
                lista.add(new Wspolrzedne(x,y));
                lista2.add(new Wspolrzedne(x,y));
            }
            else{
                for(int i=0;i<10;i++){
                    for(int j=0;j<10;j++){
                        punkt=new Wspolrzedne(i,j);                     
                        if(tablica[i][j]==StatusPola.POLE_PUSTE&&punkt.checkPoint(i,j,tablica) == true&&!listaXY2.contains(punkt))
                            listaXY.add(new Wspolrzedne(i,j));
                    }
                }
                if(listaXY.size()>1)
                    d = randomGenerator.nextInt(listaXY.size()-1);   
                else
                    d = 0;    
                x = listaXY.get(d).getX();
                y = listaXY.get(d).getY();
                //System.out.println("x=" + x + " y=" + y);
                tablica[x][y] = StatusPola.STATEK;
                lista.add(new Wspolrzedne(x,y));
                lista2.add(new Wspolrzedne(x,y));
                listaXY.clear();
            }
            //Dobor kolejnych pol statku
            for(int i=0; i<l; i++){
                if(y<9){
                    if(tablica[x][y+1]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x,y+1,tablica,3)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x==listaXY.get(k).getX()&&y+1==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x,y+1));
                         else test = false;
                    }
                }
                if(y>0){
                     if(tablica[x][y-1]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x,y-1,tablica,1)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x==listaXY.get(k).getX()&&y-1==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x,y-1));
                         else test = false;
                     }
                }
                if(x<9){
                     if(tablica[x+1][y]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x+1,y,tablica,2)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x+1==listaXY.get(k).getX()&&y==listaXY.get(k).getY())
                                 test = true;    
                         }
                        if(test==false) listaXY.add(new Wspolrzedne(x+1,y));
                        else test = false;
                     }
                }           
                if(x>0){
                     if(tablica[x-1][y]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x-1,y,tablica,4)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x-1==listaXY.get(k).getX()&&y==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x-1,y));
                         else test = false;
                     }
                }
                //Zabezpieczenie przed patem
                test = true;
                if(listaXY.isEmpty()){
                    for(int j=0; j<lista.size(); j++)
                        tablica[lista.get(j).getX()][lista.get(j).getY()] = StatusPola.POLE_PUSTE;
                    listaXY2.add(new Wspolrzedne(lista.get(0).getX(),lista.get(0).getY()));
                    test = false;
               
                    lista = new ArrayList<Wspolrzedne>();
                    lista2 = new ArrayList<Wspolrzedne>();
                    i=l;
                }    
                else{
                    if(listaXY.size()>1)
                        d = randomGenerator.nextInt(listaXY.size()-1);   
                    else
                        d = 0;
                    x = listaXY.get(d).getX();
                    y = listaXY.get(d).getY();
                    listaXY.remove(d);
                    tablica[x][y] = StatusPola.STATEK;
                    lista.add(new Wspolrzedne(x,y));
                    lista2.add(new Wspolrzedne(x,y));
                }
            }
            if(test == true){
                s++;
                if(s==1){
                    okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));
                }
                if(s==2){
                    okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));
                }
                if(s==3){
                    okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));
                }
                if(s==4){
                    okrety.add(new Jednostka(lista, Flota.Dwumasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Dwumasztowiec));
                }
                if(s==5){
                    okrety.add(new Jednostka(lista, Flota.Dwumasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Dwumasztowiec));
                }
                if(s==6){
                    okrety.add(new Jednostka(lista, Flota.Dwumasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Dwumasztowiec));
                }
                if(s==7){
                    okrety.add(new Jednostka(lista, Flota.Jednomasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Jednomasztowiec));
                }
                if(s==8){
                    okrety.add(new Jednostka(lista, Flota.Jednomasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Jednomasztowiec));
                }
                if(s==9){
                    okrety.add(new Jednostka(lista, Flota.Jednomasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Jednomasztowiec));
                }
                if(s==10){
                    okrety.add(new Jednostka(lista, Flota.Jednomasztowiec));
                    okrety2.add(new Jednostka(lista2, Flota.Jednomasztowiec));
                }
                if(s==1){
                    l--;
                    listaXY2.clear();
                }
                if(s==3){
                    l--;
                    listaXY2.clear();
                }
                if(s==6){
                    l--;
                    listaXY2.clear();
                }
                listaXY.clear();
       
                lista = new ArrayList<Wspolrzedne>();
                lista2 = new ArrayList<Wspolrzedne>();
            }
        }
        repaint();
    }
    
    /**
     * Metoda automatycznie rozstawiająca statki na planszy komputera
     */
    public void rozstawKomputer(){
        for(int i = 0; i <10; i++)
             for(int j = 0; j < 10; j++){
                 tablicaKomputer[i][j]=StatusPola.POLE_PUSTE;
                 tablicaKomputer2[i][j]=StatusPola.POLE_PUSTE;
             }
        int x,y,d,l=3,s=0;
        boolean test = false;
        Wspolrzedne punkt = new Wspolrzedne(0,0);
        ArrayList<Wspolrzedne> listaXY = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> listaXY2 = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> listaKomputer = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> lista2Komputer = new ArrayList<Wspolrzedne>();
        okretyKomputer.clear();
        okrety2Komputer.clear();
        Random randomGenerator = new Random();
        while(s<10){
            //Wybor pierwszego pola statku
            if(s==0){            
                x = randomGenerator.nextInt(10);
                y = randomGenerator.nextInt(10);
                tablicaKomputer[x][y] = StatusPola.STATEK;
                listaKomputer.add(new Wspolrzedne(x,y));
                lista2Komputer.add(new Wspolrzedne(x,y));
            }
            else{
                for(int i=0;i<10;i++){
                    for(int j=0;j<10;j++){
                        punkt=new Wspolrzedne(i,j);                     
                        if(tablicaKomputer[i][j]==StatusPola.POLE_PUSTE&&punkt.checkPoint(i,j,tablicaKomputer) == true&&!listaXY2.contains(punkt))
                            listaXY.add(new Wspolrzedne(i,j));
                    }
                }
                if(listaXY.size()>1)
                    d = randomGenerator.nextInt(listaXY.size()-1);   
                else
                    d = 0;    
                x = listaXY.get(d).getX();
                y = listaXY.get(d).getY();
                //System.out.println("x=" + x + " y=" + y);
                tablicaKomputer[x][y] = StatusPola.STATEK;
                listaKomputer.add(new Wspolrzedne(x,y));
                lista2Komputer.add(new Wspolrzedne(x,y));
                listaXY.clear();
            }
            //Dobor kolejnych pol statku
            for(int i=0; i<l; i++){
                if(y<9){
                    if(tablicaKomputer[x][y+1]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x,y+1,tablicaKomputer,3)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x==listaXY.get(k).getX()&&y+1==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x,y+1));
                         else test = false;
                    }
                }
                if(y>0){
                     if(tablicaKomputer[x][y-1]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x,y-1,tablicaKomputer,1)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x==listaXY.get(k).getX()&&y-1==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x,y-1));
                         else test = false;
                     }
                }
                if(x<9){
                     if(tablicaKomputer[x+1][y]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x+1,y,tablicaKomputer,2)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x+1==listaXY.get(k).getX()&&y==listaXY.get(k).getY())
                                 test = true;    
                         }
                        if(test==false) listaXY.add(new Wspolrzedne(x+1,y));
                        else test = false;
                     }
                }           
                if(x>0){
                     if(tablicaKomputer[x-1][y]==StatusPola.POLE_PUSTE&&punkt.checkPoint2(x-1,y,tablicaKomputer,4)==true){
                         for(int k=0; k<listaXY.size(); k++){
                             if(x-1==listaXY.get(k).getX()&&y==listaXY.get(k).getY())
                                 test = true;    
                         }
                         if(test==false) listaXY.add(new Wspolrzedne(x-1,y));
                         else test = false;
                     }
                }
                //Zabezpieczenie przed patem
                test = true;
                if(listaXY.isEmpty()){
                    for(int j=0; j<listaKomputer.size(); j++)
                        tablicaKomputer[listaKomputer.get(j).getX()][listaKomputer.get(j).getY()] = StatusPola.POLE_PUSTE;
                    listaXY2.add(new Wspolrzedne(listaKomputer.get(0).getX(),listaKomputer.get(0).getY()));
                    test = false;
               
                    listaKomputer = new ArrayList<Wspolrzedne>();
                    lista2Komputer = new ArrayList<Wspolrzedne>();
                    i=l;
                }    
                else{
                    if(listaXY.size()>1)
                        d = randomGenerator.nextInt(listaXY.size()-1);   
                    else
                        d = 0;
                    x = listaXY.get(d).getX();
                    y = listaXY.get(d).getY();
                    listaXY.remove(d);
                    tablicaKomputer[x][y] = StatusPola.STATEK;
                    listaKomputer.add(new Wspolrzedne(x,y));
                    lista2Komputer.add(new Wspolrzedne(x,y));
                }
            }
            if(test == true){
                s++;
                if(s==1){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Czteromasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Czteromasztowiec));
                }
                if(s==2){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Trójmasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Trójmasztowiec));
                }
                if(s==3){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Trójmasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Trójmasztowiec));
                }
                if(s==4){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Dwumasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Dwumasztowiec));
                }
                if(s==5){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Dwumasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Dwumasztowiec));
                }
                if(s==6){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Dwumasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Dwumasztowiec));
                }
                if(s==7){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Jednomasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Jednomasztowiec));
                }
                if(s==8){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Jednomasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Jednomasztowiec));
                }
                if(s==9){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Jednomasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Jednomasztowiec));
                }
                if(s==10){
                    okretyKomputer.add(new Jednostka(listaKomputer, Flota.Jednomasztowiec));
                    okrety2Komputer.add(new Jednostka(lista2Komputer, Flota.Jednomasztowiec));
                }
                if(s==1){
                    l--;
                    listaXY2.clear();
                }
                if(s==3){
                    l--;
                    listaXY2.clear();
                }
                if(s==6){
                    l--;
                    listaXY2.clear();
                }
                listaXY.clear();
                listaKomputer = new ArrayList<Wspolrzedne>();
                lista2Komputer = new ArrayList<Wspolrzedne>();
            }
        }
    }    
    
    /*
     * Pierwsza metoda sprawdzająca poprawność samodzielnego ustawienia floty przez gracza
     */
    public boolean sprawdzUstwienie(){
        boolean test = true;
        ArrayList<Wspolrzedne> lista = new ArrayList<Wspolrzedne>();
        ArrayList<Wspolrzedne> lista2 = new ArrayList<Wspolrzedne>();
        okrety.clear();
        okrety2.clear();
        int a = 0, b = 0, c = 0, d = 0, e = 0;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++){
                lista = new ArrayList<Wspolrzedne>();
                lista2 = new ArrayList<Wspolrzedne>();
                if(i < 7)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i + 3][j] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 3][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i + 3, j));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i + 3, j));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(j < 7)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i][j + 2] == StatusPola.STATEK && tablica[i][j + 3] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 2] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 3] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i, j + 2));
                        lista.add(new Wspolrzedne(i, j + 3));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 2));
                        lista2.add(new Wspolrzedne(i, j + 3));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(i < 9 && j < 9){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        lista.add(new Wspolrzedne(i, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                }
                if(i < 8 && j < 9){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i + 2][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i + 2, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i + 2, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK && tablica[i + 2][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        lista.add(new Wspolrzedne(i + 2, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        lista2.add(new Wspolrzedne(i + 2, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK && tablica[i + 2][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        lista.add(new Wspolrzedne(i + 2, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        lista2.add(new Wspolrzedne(i + 2, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }                    
                }    
                if(i < 8 && j > 0){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i + 2][j - 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j - 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i + 2, j - 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i + 2, j - 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK && tablica[i + 2][j - 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j - 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        lista.add(new Wspolrzedne(i + 2, j - 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        lista2.add(new Wspolrzedne(i + 2, j - 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                }     
                if(i < 9 && j < 8){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i][j + 2] == StatusPola.STATEK && tablica[i + 1][j + 2] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 2] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 2] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i, j + 2));
                        lista.add(new Wspolrzedne(i + 1, j + 2));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 2));
                        lista2.add(new Wspolrzedne(i + 1, j + 2));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i][j + 2] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 2] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i, j + 2));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 2));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i][j + 2] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 2] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i, j + 2));
                        lista.add(new Wspolrzedne(i + 1, j));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 2));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK && tablica[i + 1][j + 2] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 2] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        lista.add(new Wspolrzedne(i + 1, j + 2));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        lista2.add(new Wspolrzedne(i + 1, j + 2));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK && tablica[i + 1][j + 2] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 2] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        lista.add(new Wspolrzedne(i + 1, j + 2));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        lista2.add(new Wspolrzedne(i + 1, j + 1 + 2));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }                    
                } 
                if(i < 9 && j > 0 && j < 9){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                }    
                if(i < 9 && j > 1)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j - 2] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK){
                        d++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 2] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j - 2));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        lista.add(new Wspolrzedne(i + 1, j));
                        okrety.add(new Jednostka(lista, Flota.Czteromasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 2));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        okrety2.add(new Jednostka(lista2, Flota.Czteromasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }                
                if(i < 9 && j < 9){
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 1][j + 1] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                    else if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i + 1][j +1] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i + 1, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i + 1, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                }
                if(i < 8)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK && tablica[i + 2][j] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 2][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        lista.add(new Wspolrzedne(i + 2, j));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        lista2.add(new Wspolrzedne(i + 2, j));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(j < 8)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK && tablica[i][j + 2] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 2] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        lista.add(new Wspolrzedne(i, j + 2));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        lista2.add(new Wspolrzedne(i, j + 2));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(i < 9 && j >0)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j - 1] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK){
                        c++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j - 1] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j - 1));
                        lista.add(new Wspolrzedne(i + 1, j));
                        okrety.add(new Jednostka(lista, Flota.Trójmasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j - 1));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        okrety2.add(new Jednostka(lista2, Flota.Trójmasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(i < 9)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i + 1][j] == StatusPola.STATEK){
                        b++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i + 1][j] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i + 1, j));
                        okrety.add(new Jednostka(lista, Flota.Dwumasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i + 1, j));
                        okrety2.add(new Jednostka(lista2, Flota.Dwumasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(j < 9)
                    if(tablica[i][j] == StatusPola.STATEK && tablica[i][j + 1] == StatusPola.STATEK){
                        b++;
                        tablica[i][j] = StatusPola.SPRAWDZONY;
                        tablica[i][j + 1] = StatusPola.SPRAWDZONY;
                        lista.add(new Wspolrzedne(i, j));
                        lista.add(new Wspolrzedne(i, j + 1));
                        okrety.add(new Jednostka(lista, Flota.Dwumasztowiec));
                        lista2.add(new Wspolrzedne(i, j));
                        lista2.add(new Wspolrzedne(i, j + 1));
                        okrety2.add(new Jednostka(lista2, Flota.Dwumasztowiec));                        
                        if(!sprawdzUstawienie2())
                            test = false;
                    }
                if(tablica[i][j] == StatusPola.STATEK){
                    a++;
                    tablica[i][j] = StatusPola.SPRAWDZONY;
                    lista.add(new Wspolrzedne(i, j));
                    okrety.add(new Jednostka(lista, Flota.Jednomasztowiec));
                    lista2.add(new Wspolrzedne(i, j));
                    okrety2.add(new Jednostka(lista2, Flota.Jednomasztowiec));                    
                    if(!sprawdzUstawienie2())
                            test = false;
                }
            }
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(tablica[i][j] == StatusPola.STATEK)
                    e++;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(tablica[i][j] == StatusPola.SPRAWDZONY)
                    tablica[i][j] = StatusPola.STATEK;   
        if(a == 4 && b == 3 && c == 2 && d == 1 && e == 0 && test){
            getGui().getZdarzenie().dobreUstawienie();
            getGui().setRozpocznijGre(true);
            return true;
        }
        else {
            getGui().getZdarzenie().zleUstawienie();
            return false;
        }
    }
    /*
     * Druga metoda sprawdzająca poprawność samodzielnego ustawienia floty przez gracza
     */    
    public boolean sprawdzUstawienie2(){
        boolean test = true;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(tablica[i][j] == StatusPola.SPRAWDZONY){
                    if(i>0&&j>0)
                        if(tablica[i-1][j-1] == StatusPola.STATEK){
                            test = false;
                        }
                    if(i>0)
                        if(tablica[i-1][j] == StatusPola.STATEK){
                            test = false;
                        }
                    if(j>0)
                        if(tablica[i][j-1] == StatusPola.STATEK){
                            test = false;
                        }
                    if(i<9&&j<9)
                        if(tablica[i+1][j+1] == StatusPola.STATEK){
                            test = false;
                        }
                    if(i<9)
                        if(tablica[i+1][j] == StatusPola.STATEK){
                            test = false;
                        }
                    if(j<9)
                        if(tablica[i][j+1] == StatusPola.STATEK){
                            test = false;
                        }
                    if(i>0&&j<9)
                        if(tablica[i-1][j+1] == StatusPola.STATEK){
                            test = false;
                        }
                    if(i<9&&j>0){
                        if(tablica[i+1][j-1] == StatusPola.STATEK){
                            test = false;
                        }
                    }
                }
        return test;
    }
    
    /**
     * Metoda symulująca myślenie, opuźniająca strzał komputera
     */
    public void strzalKomputer(){
        timer = new Timer();
        timer.schedule(new RemindTask(), 3000);        
    }    
    
    /**
     * Metoda wykonująca strzał komputera
     */
    public void strzalKomputer2(){      
        int d, x, y, s=0;
        Random randomGenerator = new Random();
        if(traf){
            if(listKomp2.size()>1)
                d = randomGenerator.nextInt(listKomp2.size()-1);   
            else
                d = 0;
            x = listKomp2.get(d).getX();
            y = listKomp2.get(d).getY();            
        }
        else{
            if(listKomp.isEmpty()){
                for(int i = 0; i <10; i++)
                    for(int j = 0; j < 10; j++)
                        if(tablicaKomputer2[i][j]==StatusPola.POLE_PUSTE)
                            listKomp.add(new Wspolrzedne(i,j));
            }
            if(listKomp.size()>1)
                d = randomGenerator.nextInt(listKomp.size()-1);   
            else
                d = 0;
            x = listKomp.get(d).getX();
            y = listKomp.get(d).getY();            
        }
        if(sprawdzStrzal(x, y) == WynikStrzalu.TRAFIONY_ZATOPIONY){
            zaznaczZatopionyKomputer(x, y);
            dialogTrafiony(x,y);
            listKomp.clear();
            listKomp2.clear();
            traf = false;
            getGui().setStatkiGracza();
            if(getGui().getStatkiGracza()==10){
                getGui().getZdarzenie().przegralem();
                getGui().setKoniec("Rozpocznij grę");
            }
        }
        else if(sprawdzStrzal(x,y) == WynikStrzalu.TRAFIONY){
            oznaczPole(x,y,sprawdzStrzal(x,y));
            dialogTrafiony(x,y);            
            tablicaKomputer2[x][y]=StatusPola.STATEK_TRAFIONY;
            if(traf)
                listKomp2.remove(d);
            traf = true;
            if(x>0&&tablicaKomputer2[x-1][y] == StatusPola.POLE_PUSTE)
                listKomp2.add(new Wspolrzedne(x-1,y));
            if(x<9&&tablicaKomputer2[x+1][y] == StatusPola.POLE_PUSTE)
                listKomp2.add(new Wspolrzedne(x+1,y));
            if(y>0&&tablicaKomputer2[x][y-1] == StatusPola.POLE_PUSTE)
                listKomp2.add(new Wspolrzedne(x,y-1));
            if(y<9&&tablicaKomputer2[x][y+1] == StatusPola.POLE_PUSTE)
                listKomp2.add(new Wspolrzedne(x,y+1));   
        }
        else{
            if(traf)
                listKomp2.remove(d);
            else
                listKomp.remove(d);
            oznaczPole(x,y,sprawdzStrzal(x,y));
            getGui().getZdarzenie().komunikatTrafionego("nic", 0);
            tablicaKomputer2[x][y]=StatusPola.PUDLO;
            getGui().setToken(true);
        }
        if(getGui().getToken() == false && getGui().getStatkiGracza()!=10)
            strzalKomputer();
    }    
    
    /**
     * Metoda obsługująca strzał gracza
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void strzalGracz(int x, int y){
        if(sprawdzStrzalKomp(x,y) == WynikStrzalu.TRAFIONY_ZATOPIONY){
            zaznaczZatopionyGracz(x, y);
            dialogTrafiajacy(x,y);
            getGui().setStatkiPrzeciwnika();
            if(getGui().getStatkiPrzeciwnika()==10){
                getGui().getZdarzenie().wygralem();
                getGui().setKoniec("Rozpocznij grę");
            }
        }
        else if(sprawdzStrzalKomp(x,y) == WynikStrzalu.TRAFIONY){
            getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.TRAFIONY);
            dialogTrafiajacy(x,y);
        }
        else{
            getGui().getPoleRywala().oznaczPole(x, y, WynikStrzalu.PUDLO);
            getGui().getZdarzenie().komunikatTrafiajacego(5, 5);
            getGui().setToken(false);
        }
        if(getGui().getToken() == false && getGui().getStatkiPrzeciwnika()!=10)
            strzalKomputer();
    }
    
    /**
     * Metoda obsługująca komunikaty wyświetlane w oknie dialogowym trafiającego
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void dialogTrafiajacy(int x, int y){
        String k = getGui().getPoleGracza().typKomputer(x, y);
        if(k.equals("Jednomasztowiec")){
            Random randomGenerator = new Random();
            int b = randomGenerator.nextInt(3);
            getGui().getZdarzenie().komunikatTrafiajacego(1, b);
        }
        else if(k.equals("Dwumasztowiec")) {
            int q = getGui().getPoleGracza().stopienKomputer(x, y);
            getGui().getZdarzenie().komunikatTrafiajacego(2, q);
        }
        else if(k.equals("Trójmasztowiec")) {
            int q = getGui().getPoleGracza().stopienKomputer(x, y);
            getGui().getZdarzenie().komunikatTrafiajacego(3, q);
        }
        else {
            int q = getGui().getPoleGracza().stopienKomputer(x, y);
            getGui().getZdarzenie().komunikatTrafiajacego(4, q);
        }                
    }
    
    /**
     * Metoda obsługująca komunikaty wyświetlane w oknie dialogowym trafionego
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void dialogTrafiony(int x, int y){
        String k = typ(x, y);
        if(k.equals("Jednomasztowiec")){
            Random randomGenerator = new Random();
            int b = randomGenerator.nextInt(3);
            getGui().getZdarzenie().komunikatTrafionego(k, b);
        }
        else{
            int q = stopien(x, y);
            getGui().getZdarzenie().komunikatTrafionego(k, q);
        }              
    }    
    
    /**
     * Metoda sprawdza strzał gracza i zwraca wynik strzału
     * @param x współrzędna x
     * @param y współrzędna y
     * @return wynik strzału gracza
     */
    public WynikStrzalu sprawdzStrzal(int x, int y) {
        for(int i=0;i<okrety.size();i++)
            for(int j=0; j<okrety.get(i).getLista().size();j++)
                if(x==okrety.get(i).getLista().get(j).getX()&&y==okrety.get(i).getLista().get(j).getY()&&okrety.get(i).getLista().size()==1)
                    return WynikStrzalu.TRAFIONY_ZATOPIONY;
        if (tablica[x][y] == StatusPola.STATEK)
            return WynikStrzalu.TRAFIONY;
        else
            return WynikStrzalu.PUDLO;
    }

    /**
     * Metoda sprawdza strzał komputera i zwraca wynik strzału
     * @param x współrzędna x
     * @param y współrzędna y
     * @return wynik strzału komputera
     */    
    public WynikStrzalu sprawdzStrzalKomp(int x, int y) {
        for(int i=0;i<okretyKomputer.size();i++)
            for(int j=0; j<okretyKomputer.get(i).getLista().size();j++)
                if(x==okretyKomputer.get(i).getLista().get(j).getX()&&y==okretyKomputer.get(i).getLista().get(j).getY()&&okretyKomputer.get(i).getLista().size()==1)
                    return WynikStrzalu.TRAFIONY_ZATOPIONY;
        if (tablicaKomputer[x][y] == StatusPola.STATEK)
            return WynikStrzalu.TRAFIONY;
        else
            return WynikStrzalu.PUDLO;
    }
    
    /**
     * Metoda oznaczająca pole na planszy po strzale
     * @param x współrzędna x
     * @param y współrzędna y
     * @param w wynik strzału
     */
    public void oznaczPole(int x, int y, WynikStrzalu w){
        if(w==WynikStrzalu.TRAFIONY_ZATOPIONY)
            tablica[x][y] = StatusPola.STATEK_ZATOPIONY;
        else if(w==WynikStrzalu.PUDLO)
            tablica[x][y] = StatusPola.PUDLO;
        else if(w==WynikStrzalu.TRAFIONY)
            tablica[x][y]=StatusPola.STATEK_TRAFIONY;
        else
            tablica[x][y] = StatusPola.PUSTE_AUTOMAT;
        repaint();
    }   
    
    /**
     * Metoda oznaczająca pole jako niewiadome przed puszczeniem przycisku myszy
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void oznaczNiewiadome(int x, int y){
        tablica[x][y] = StatusPola.NIEWIADOMY;
        repaint();
    }
    
    /**
     * Metoda oznaczająca pole na planszy jako puste
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void oznaczPuste(int x, int y){
        if(tablica[x][y] == StatusPola.NIEWIADOMY)    
            tablica[x][y] = StatusPola.POLE_PUSTE;
        repaint();
    }
    
    /**
     * Metoda zwracająca poziom uszkodzenia statku gracza
     * @param x współrzędna x
     * @param y współrzędna y
     * @return poziom uszkodzenia statku gracza
     */
    public int stopien(int x, int y){ 
       for(int i=0;i<okrety.size();i++)
            for(int j=0; j<okrety.get(i).getLista().size();j++)
                if(x==okrety.get(i).getLista().get(j).getX()&&y==okrety.get(i).getLista().get(j).getY()){
                    rozmiar = okrety.get(i).getLista().size();
                    okrety.get(i).getLista().remove(j);
                }
        return rozmiar;
    }

    /**
     * Metoda zwracająca poziom uszkodzenia statku komputera
     * @param x współrzędna x
     * @param y współrzędna y
     * @return poziom uszkodzenia statku komputera
     */    
    public int stopienKomputer(int x, int y){ 
       for(int i=0;i<okretyKomputer.size();i++)
            for(int j=0; j<okretyKomputer.get(i).getLista().size();j++)
                if(x==okretyKomputer.get(i).getLista().get(j).getX()&&y==okretyKomputer.get(i).getLista().get(j).getY()){
                    rozmiar = okretyKomputer.get(i).getLista().size();
                    okretyKomputer.get(i).getLista().remove(j);
                }
        return rozmiar;
    }
    
    /**
     * Metoda zwracająca typ statku gracza
     * @param x współrzędna x
     * @param y współrzędna y
     * @return typ statku gracza
     */
    public String typ(int x, int y){
       for(int i=0;i<okrety2.size();i++)
            for(int j=0; j<okrety2.get(i).getLista().size();j++)
                if(x==okrety2.get(i).getLista().get(j).getX()&&y==okrety2.get(i).getLista().get(j).getY())
                    nazwa = okrety2.get(i).getNazwa().toString();
        return nazwa;        
    }

    /**
     * Metoda zwracająca typ statku komputera
     * @param x współrzędna x
     * @param y współrzędna y
     * @return typ statku komputera
     */    
    public String typKomputer(int x, int y){
       for(int i=0;i<okrety2Komputer.size();i++)
            for(int j=0; j<okrety2Komputer.get(i).getLista().size();j++)
                if(x==okrety2Komputer.get(i).getLista().get(j).getX()&&y==okrety2Komputer.get(i).getLista().get(j).getY())
                    nazwa = okrety2Komputer.get(i).getNazwa().toString();
        return nazwa;        
    }
    
    /**
     * Metoda zaznacza zatopienie statku gracza w trybie gry sieciowej
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void zaznaczZatopiony(int x, int y){
        int q,w;
        getGui().setStatkiGracza();
        if(getGui().getSerw()){
            getGui().getZdarzenie().getSerwer().nadaj("?");
            getGui().getZdarzenie().getSerwer().nadaj("/*/*");
        }
        else{
            getGui().getZdarzenie().getKlient().nadaj("?");                            
            getGui().getZdarzenie().getKlient().nadaj("/*/*");
        }
        for(int i=0;i<okrety2.size();i++)
            for(int j=0; j<okrety2.get(i).getLista().size();j++)
                if(x==okrety2.get(i).getLista().get(j).getX()&&y==okrety2.get(i).getLista().get(j).getY()){
                    for(int k=0; k<okrety2.get(i).getLista().size(); k++){
                        q=okrety2.get(i).getLista().get(k).getX();
                        w=okrety2.get(i).getLista().get(k).getY();
                        if(getGui().getSerw()){
                            tablica[q][w] = StatusPola.STATEK_ZATOPIONY;
                            getGui().getZdarzenie().getSerwer().nadaj("!!!" + q + w);
                        }
                        else{
                            tablica[q][w] = StatusPola.STATEK_ZATOPIONY;
                            getGui().getZdarzenie().getKlient().nadaj("!!!" + q + w);
                        }
                    }
                    for(int k=0; k<okrety2.get(i).getLista().size(); k++){
                        q=okrety2.get(i).getLista().get(k).getX();
                        w=okrety2.get(i).getLista().get(k).getY();
                        if(q>0&&w>0)
                            if(tablica[q-1][w-1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q-1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q-1) + (w-1));
                                    czekaj();
                                }
                                else{
                                    tablica[q-1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q-1) + (w-1));                            
                                    czekaj();
                                }
                            }
                        if(q>0)
                            if(tablica[q-1][w]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q-1][w] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q-1) + w);
                                    czekaj();
                                }
                                else{
                                    tablica[q-1][w] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q-1) + w);                                
                                    czekaj();
                                }
                            }
                        if(w>0)
                            if(tablica[q][w-1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + q + (w-1));
                                    czekaj();
                                }
                                else{
                                    tablica[q][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + q + (w-1));                                
                                    czekaj();
                                }
                            }
                        if(q<9&&w>0)
                            if(tablica[q+1][w-1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q+1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q+1) + (w-1));
                                    czekaj();
                                }
                                else{
                                    tablica[q+1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q+1) + (w-1));                                
                                    czekaj();
                                }
                            }
                        if(q<9)
                            if(tablica[q+1][w]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q+1][w] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q+1) + w);
                                    czekaj();
                                }
                                else{
                                    tablica[q+1][w] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q+1) + w);                                
                                    czekaj();
                                }
                            }
                        if(q<9&&w<9)
                            if(tablica[q+1][w+1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q+1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q+1) + (w+1));
                                    czekaj();
                                }
                                else{
                                    tablica[q+1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q+1) + (w+1));                                
                                    czekaj();
                                }
                            }
                        if(w<9)
                            if(tablica[q][w+1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + q + (w+1));
                                    czekaj();
                                }
                                else{
                                    tablica[q][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + q + (w+1));                                
                                    czekaj();
                                }
                            }
                        if(q>0&&w<9)
                            if(tablica[q-1][w+1]==StatusPola.POLE_PUSTE){
                                if(getGui().getSerw()){
                                    tablica[q-1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getSerwer().nadaj("&&&" + (q-1) + (w+1));
                                    czekaj();
                                }
                                else{
                                    tablica[q-1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    getGui().getZdarzenie().getKlient().nadaj("&&&" + (q-1) + (w+1));                                
                                    czekaj();
                                }
                            }
                    }                    
                }
        if(getGui().getSerw())
            getGui().getZdarzenie().getSerwer().nadaj("*/*/"); 
        else               
            getGui().getZdarzenie().getKlient().nadaj("*/*/");
        repaint();
    }

     /**
     * Metoda zaznacza zatopienie statku komputera
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public void zaznaczZatopionyKomputer(int x, int y){
        int q,w;
        for(int i=0;i<okrety2.size();i++)
            for(int j=0; j<okrety2.get(i).getLista().size();j++)
                if(x==okrety2.get(i).getLista().get(j).getX()&&y==okrety2.get(i).getLista().get(j).getY()){
                    for(int k=0; k<okrety2.get(i).getLista().size(); k++){
                        q=okrety2.get(i).getLista().get(k).getX();
                        w=okrety2.get(i).getLista().get(k).getY();
                            tablica[q][w] = StatusPola.STATEK_ZATOPIONY;
                            tablicaKomputer2[q][w] = StatusPola.STATEK_ZATOPIONY;
                            czekaj();
                    }
                    for(int k=0; k<okrety2.get(i).getLista().size(); k++){
                        q=okrety2.get(i).getLista().get(k).getX();
                        w=okrety2.get(i).getLista().get(k).getY();
                        if(q>0&&w>0)
                            if(tablica[q-1][w-1]==StatusPola.POLE_PUSTE){
                                    tablica[q-1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q-1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(q>0)
                            if(tablica[q-1][w]==StatusPola.POLE_PUSTE){
                                    tablica[q-1][w] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q-1][w] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(w>0)
                            if(tablica[q][w-1]==StatusPola.POLE_PUSTE){
                                    tablica[q][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(q<9&&w>0)
                            if(tablica[q+1][w-1]==StatusPola.POLE_PUSTE){
                                    tablica[q+1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q+1][w-1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(q<9)
                            if(tablica[q+1][w]==StatusPola.POLE_PUSTE){
                                    tablica[q+1][w] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q+1][w] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();                                
                            }
                        if(q<9&&w<9)
                            if(tablica[q+1][w+1]==StatusPola.POLE_PUSTE){
                                    tablica[q+1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q+1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(w<9)
                            if(tablica[q][w+1]==StatusPola.POLE_PUSTE){
                                    tablica[q][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                        if(q>0&&w<9)
                            if(tablica[q-1][w+1]==StatusPola.POLE_PUSTE){
                                    tablica[q-1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    tablicaKomputer2[q-1][w+1] = StatusPola.PUSTE_AUTOMAT;
                                    czekaj();
                            }
                    }                    
                } 
    }    
    
    /**
     * Metoda służąca do opóźnienia wykonania programu podczas zaznaczania pól pustych
     * @exception Metoda może zwrócić wyjątek InterruptedException
     */
    public void czekaj(){
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
        }
        repaint();        
    }

    /**
     * Metoda zaznacza zatopienie statku gracza w trybie gry z komputerem
     * @param x współrzędna x
     * @param y współrzędna y
     */    
    public void zaznaczZatopionyGracz(int x, int y){
        int q,w;
        for(int i=0;i<okrety2Komputer.size();i++)
            for(int j=0; j<okrety2Komputer.get(i).getLista().size();j++)
                if(x==okrety2Komputer.get(i).getLista().get(j).getX()&&y==okrety2Komputer.get(i).getLista().get(j).getY()){
                    for(int k=0; k<okrety2Komputer.get(i).getLista().size(); k++){
                        q=okrety2Komputer.get(i).getLista().get(k).getX();
                        w=okrety2Komputer.get(i).getLista().get(k).getY();
                            getGui().getPoleRywala().oznaczPole(q,w, WynikStrzalu.TRAFIONY_ZATOPIONY);
                    }
                    for(int k=0; k<okrety2Komputer.get(i).getLista().size(); k++){
                        q=okrety2Komputer.get(i).getLista().get(k).getX();
                        w=okrety2Komputer.get(i).getLista().get(k).getY();
                        if(q>0&&w>0)
                            if(getGui().getPoleRywala().getTablica()[q-1][w-1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q-1,w-1, null);
                        if(q>0)
                            if(getGui().getPoleRywala().getTablica()[q-1][w]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q-1,w, null);
                        if(w>0)
                            if(getGui().getPoleRywala().getTablica()[q][w-1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q,w-1, null);
                        if(q<9&&w>0)
                            if(getGui().getPoleRywala().getTablica()[q+1][w-1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q+1,w-1, null);
                        if(q<9)
                            if(getGui().getPoleRywala().getTablica()[q+1][w]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q+1,w, null);
                        if(q<9&&w<9)
                            if(getGui().getPoleRywala().getTablica()[q+1][w+1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q+1,w+1, null);
                        if(w<9)
                            if(getGui().getPoleRywala().getTablica()[q][w+1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q,w+1, null);
                        if(q>0&&w<9)
                            if(getGui().getPoleRywala().getTablica()[q-1][w+1]==StatusPola.POLE_PUSTE)
                                    getGui().getPoleRywala().oznaczPole(q-1,w+1, null);
                    }                    
                } 
        repaint();
    } 
    
    /**
     * Konstruktor klasy PoleBitwy
     * @param wlasciciel właściciel planszy
     * @param gui GUI programu
     */
    public PoleBitwy(final Gracze wlasciciel, GUI gui) {        
	this.gui = gui; 
        Dimension rozmiar = new Dimension(302, 302);
	setSize(rozmiar);
	setMinimumSize(rozmiar);
	setMaximumSize(rozmiar);
	setPreferredSize(rozmiar);
	this.wlasciciel = wlasciciel;
	zerowanieTablicy();
	if (wlasciciel == Gracze.GRACZ)
            rozstaw();
        addMouseMotionListener(new MouseAdapter() {
        public void mouseMoved(MouseEvent e){
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
        }
        }); 
        addMouseListener(new MouseAdapter() { 
        int x, y, z, w;
        public void mousePressed(MouseEvent e){
            if (e.getButton() == MouseEvent.BUTTON1 && wlasciciel == Gracze.PRZECIWNIK && getGui().getToken() && getGui().getGraKomp() == false) {
        	Point p = e.getPoint();
        	if (p.x % 30 != 0 && p.x % 30 != 1 && p.y % 30 != 0 && p.y % 30 != 1) {
                    x = (p.x - 1) / 30;
                    y = (p.y - 1) / 30;
                    if (getGui().getPoleRywala().tablica[x][y] == StatusPola.POLE_PUSTE) {  
                        getGui().getPoleRywala().oznaczNiewiadome(x, y);                       
                    }
                }
            }            
            if (e.getButton() == MouseEvent.BUTTON1 && wlasciciel == Gracze.PRZECIWNIK && getGui().getToken() && getGui().getGraKomp() == true){
            	Point p = e.getPoint();
        	if (p.x % 30 != 0 && p.x % 30 != 1 && p.y % 30 != 0 && p.y % 30 != 1) {
                    x = (p.x - 1) / 30;
                    y = (p.y - 1) / 30;
                    if (getGui().getPoleRywala().tablica[x][y] == StatusPola.POLE_PUSTE) {
                        getGui().getPoleRywala().oznaczNiewiadome(x, y);
                    }
                }                
            }            
        }    
                
	public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && wlasciciel == Gracze.PRZECIWNIK && getGui().getToken() && getGui().getGraKomp() == false) {
        	Point p = e.getPoint();                
                if (p.x % 30 != 0 && p.x % 30 != 1 && p.y % 30 != 0 && p.y % 30 != 1) {
                    w = (p.x - 1) / 30;
                    z = (p.y - 1) / 30;
                    if (w < 10 && z < 10 && w>=0 && z>=0 && getGui().getPoleRywala().tablica[w][z] == StatusPola.NIEWIADOMY && x == w && y == z) {           
                        if(getGui().getSerw())
                            getGui().getZdarzenie().getSerwer().nadaj("" + w + z + "");
                        else
                            getGui().getZdarzenie().getKlient().nadaj("" + w + z + "");                        
                    }
                    else{
                        getGui().getPoleRywala().oznaczPuste(x, y);
                    }
                }
                else{
                    getGui().getPoleRywala().oznaczPuste(x, y);
                }
            }
            else if (e.getButton() == MouseEvent.BUTTON1 && wlasciciel == Gracze.PRZECIWNIK && getGui().getToken() && getGui().getGraKomp() == true){
        	Point p = e.getPoint();
        	if (p.x % 30 != 0 && p.x % 30 != 1 && p.y % 30 != 0 && p.y % 30 != 1) {
                    w = (p.x - 1) / 30;
                    z = (p.y - 1) / 30;
                    if (w < 10 && z < 10 && w>=0 && z>=0 && getGui().getPoleRywala().tablica[w][z] == StatusPola.NIEWIADOMY && x == w && y == z) {
                        if(e.getButton() == MouseEvent.BUTTON1_DOWN_MASK){
                            getGui().getPoleRywala().oznaczNiewiadome(w, z);
                        }
                        getGui().getPoleGracza().strzalGracz(w, z);
                    }
                    else{
                        getGui().getPoleRywala().oznaczPuste(x, y);
                    }
                }
                else{
                    getGui().getPoleRywala().oznaczPuste(x, y);
                }                
            }
            else if (e.getButton() == MouseEvent.BUTTON1 && wlasciciel == Gracze.GRACZ && getGui().getRozstawFlote()){
        	Point p = e.getPoint();
        	if (p.x % 30 != 0 && p.x % 30 != 1 && p.y % 30 != 0 && p.y % 30 != 1) {
                    int x = (p.x - 1) / 30;
                    int y = (p.y - 1) / 30;
                    if(tablica[x][y]==StatusPola.POLE_PUSTE)
                        tablica[x][y]=StatusPola.STATEK;
                    else
                        tablica[x][y]=StatusPola.POLE_PUSTE;
                    repaint();
                }                
            }            
	}

        
	});        
    }
    
    @Override
    public void paint(Graphics g) {      
	Image img = createImage(getSize().width, getSize().height);
	Graphics2D g2 = (Graphics2D) img.getGraphics();
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setColor(new Color(94, 174, 255));
	g2.fillRect(0, 0, 302, 302);
	for (int i = 0; i < 10; i++) {
		for (int j = 0; j < 10; j++) {
                    if (tablica[i][j] == StatusPola.NIEWIADOMY)
                        g2.setColor(new Color (255,250,205));
                    else
                        g2.setColor(new Color(255, 255, 255));
                    
                    g2.fillRect(2 + 30 * i, 2 + 30 * j, 28, 28);
                    if (tablica[i][j] == StatusPola.STATEK){    
                        g2.setStroke(new BasicStroke(2.0f));
                        g2.setColor(new Color(0, 0, 151));
                        g2.drawRect(2 + 30 * i, 2 + 30 * j, 27, 27);
                    }
                    else if (tablica[i][j] == StatusPola.STATEK_TRAFIONY || tablica[i][j] == StatusPola.STATEK_ZATOPIONY) {
			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(new Color(0, 0, 151));
			g2.drawLine(2 + 30 * i, 2 + 30 * j, 29 + 30 * i, 29 + 30 * j);
			g2.drawLine(29 + 30 * i, 2 + 30 * j, 2 + 30 * i, 29 + 30 * j);
                        g2.drawRect(2 + 30 * i, 2 + 30 * j, 27, 27);                        
                    }
                    else if (tablica[i][j] == StatusPola.NIEWIADOMY) {
                        g2.setFont(new Font("Dialog", Font.BOLD, 24));
                        g2.setColor(new Color(50, 50, 50));
			g2.drawString("?", 8 + 30 * i, 24 + 30 * j);
                    }
                    else if (tablica[i][j] == StatusPola.PUDLO || tablica[i][j] == StatusPola.PUSTE_AUTOMAT) {
                        g2.setColor(new Color(0, 0, 151));
                        g2.fillOval(12 + 30 * i, 12 + 30 * j, 8, 8);
                    }                    
		}
	}
		g.drawImage(img, 0, 0, this); 
	}

        /**
         * Metoda zwraca GUI programu
         * @return GUI programu
         */
        private GUI getGui(){
            return gui;
        }
        
        /**
         * Metoda zwraca status pola
         * @return status pola
         */
        public StatusPola[][] getTablica(){
            return tablica;
        }
        
	@Override
	public void update(Graphics g) {
            paint(g);
	}
        
    class RemindTask extends TimerTask {
        public void run() {
            if(getGui().getGraKompStart())
                strzalKomputer2();
        }
    }
}