package gra;

/**
 * Klasa służy do operacji na współrzędnych planszy
 * @author Grzegorz Orczykowski
 * @author grzesiek@mat.umk.pl
 * @version 1.0 beta
 */
public class Wspolrzedne {
    /** Zmienna zawierająca współrzędną x */
    private int x;
    /** Zmienna zawierająca współrzędną y */
    private int y;
    
    /**
     * Konstruktor klasy Wspolrzedne
     * @param x współrzędna x
     * @param y współrzędna y
     */
    public Wspolrzedne(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Metoda zwraca współrzędną x
     * @return współrzędna x
     */
    public int getX(){
        return x;
    }
    
    /**
     * Metoda zwraca współrzędną y
     * @return współrzędna y
     */
    public int getY(){
        return y;
    }
    
    /**
     * Metoda sprawdza otoczenie punktu i zwraca wynik testu
     * @param x współrzędna x
     * @param y współrzędna y
     * @param tablica tablica odwzorowująca planszę
     * @return true jeśli punk nie graniczy z żadnym statkiem, w przeciwnym razie false
     */
    public boolean checkPoint(int x, int y, StatusPola[][] tablica){
        boolean test = true;
        if(x>0&&y>0){
            if(tablica[x-1][y-1]!=StatusPola.POLE_PUSTE)
                test = false;
        }
        if(y>0){
            if(tablica[x][y-1]!=StatusPola.POLE_PUSTE)
                test = false;
        }
        if(x>0){
            if(tablica[x-1][y]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(x<9&&y>0){
            if(tablica[x+1][y-1]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(x<9){
            if(tablica[x+1][y]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(x<9&&y<9){
            if(tablica[x+1][y+1]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(y<9){
            if(tablica[x][y+1]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(x>0&&y<9){
            if(tablica[x-1][y+1]!=StatusPola.POLE_PUSTE)
               test = false;
        }
        if(test == true)
            return true;
        else return false;
    }
    
    /**
     * Metoda spraedza sąsiadów punktu w podanym kierunku i zwraca wynik testu
     * @param x współrzędna x
     * @param y współrzędna y
     * @param tablica tablica odwzorowująca planszę
     * @param kierunek kierunek przeprowadzanego testu
     * @return true jeśli punk nie graniczy z żadnym statkiem w podanym kierunku, w przeciwnym razie false
     */
    public boolean checkPoint2(int x, int y, StatusPola[][] tablica, int kierunek){
        boolean test = true;
        if(kierunek==1){
            if(y>0&&x>0)
                if(tablica[x-1][y-1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(y>0)
                if(tablica[x][y-1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(y>0&&x<9)
                if(tablica[x+1][y-1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
        }
        if(kierunek==2){
            if(x<9&&y>0)
                if(tablica[x+1][y-1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(x<9)
                if(tablica[x+1][y]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(x<9&&y<9)
                if(tablica[x+1][y+1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
        }
        if(kierunek==3){
            if(y<9&&x>0)
                if(tablica[x-1][y+1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(y<9)
                if(tablica[x][y+1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(y<9&&x<9)
                if(tablica[x+1][y+1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
        }
        if(kierunek==4){
            if(x>0&&y>0)
                if(tablica[x-1][y-1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(x>0)
                if(tablica[x-1][y]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
            if(x>0&&y<9)
                if(tablica[x-1][y+1]!=StatusPola.POLE_PUSTE){
                    test = false;
                }
        }
        if(test == true)
            return true;
        else return false;
    }
}
