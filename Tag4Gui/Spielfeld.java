package TicTacToe.Tag4Gui;

// Klasse für die Spiellogik und der Zustand des Feldes (3*3)
public class Spielfeld {
    private final char[][] felder;

    // Konstruktor: erzeugt ein neues, leeres Speilfeld
    public Spielfeld() {
        this.felder = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                felder[i][j] = ' ';
            }
        }
    }

    // setzt ein Symbol direkt an die angegebenen Koordinaten
    public void setzeZeichenDirekt(int zeile, int spalte, char symbol) {
        felder[zeile][spalte] = symbol;
    }

    // prüft, ob ein bestimmtes Feld noch frei ist
    public boolean feldFreiDirekt(int zeile, int spalte) {
        return felder[zeile][spalte] == ' ';
    }

    // Prüft alle Reihen, Spalten und Diagonalen auf einen Sieg
    public boolean pruefeSieg(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (felder[i][0] == symbol && felder[i][1] == symbol && felder[i][2] == symbol) return true;
            if (felder[0][i] == symbol && felder[1][i] == symbol && felder[2][i] == symbol) return true;
        }
        if (felder[0][0] == symbol && felder[1][1] == symbol && felder[2][2] == symbol) return true;
        if (felder[0][2] == symbol && felder[1][1] == symbol && felder[2][0] == symbol) return true;
        return false;
    }

    // Findet die Koordinaten der Gewinnreihe für die Animation.
    // @return Ein Array mit 3 Koordinaten-Paaren oder null, wenn kein Sieg vorliegt.
    public int[][] getGewinnZeilen(char s) {
        for (int i = 0; i < 3; i++) {
            if (felder[i][0] == s && felder[i][1] == s && felder[i][2] == s) return new int[][]{{i, 0}, {i, 1}, {i, 2}};
            if (felder[0][i] == s && felder[1][i] == s && felder[2][i] == s) return new int[][]{{0, i}, {1, i}, {2, i}};
        }
        if (felder[0][0] == s && felder[1][1] == s && felder[2][2] == s) return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        if (felder[0][2] == s && felder[1][1] == s && felder[2][0] == s) return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        return null;
    }

    // Gibt das Symbol an einer bestimmten Position zurück.
    public char getSymbol(int zeile, int spalte) {
        return felder[zeile][spalte];
    }
}