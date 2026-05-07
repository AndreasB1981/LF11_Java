package TicTacToe.Tag4Gui;

import javax.swing.*;
import java.awt.*;

// Hauptklasse der GUI: Erstellt das Fenster und steuert die Benutzerinteraktion.
public class TicTacToeGUI extends JFrame {
    private Spielfeld logik;
    private JButton[][] buttons;
    private JLabel statusLabel, punkteLabel;

    private String name1, name2;
    private char symbolSpieler1; // Merkt sich, ob Spieler 1 X oder O ist
    private int siegeSpieler1 = 0, siegeSpieler2 = 0, zuege = 0;

    private char aktuellerSpieler;
    private char starterDerRunde;
    private boolean spielAktiv = true;

    // Konstruktor: Fragt Namen und Symbole ab und baut das Fenster-Layout auf.
    public TicTacToeGUI() {
        // 1. Namen abfragen
        name1 = JOptionPane.showInputDialog(this, "Name für Spieler 1:");
        name2 = JOptionPane.showInputDialog(this, "Name für Spieler 2:");
        if (name1 == null || name1.isEmpty()) name1 = "Spieler 1";
        if (name2 == null || name2.isEmpty()) name2 = "Spieler 2";

        // 2. Symbol-Wahl für Spieler 1
        Object[] optionen = {"X", "O"};
        int wahl = JOptionPane.showOptionDialog(this, name1 + ", welches Symbol möchtest du?",
                "Symbolwahl", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);

        if (wahl == 1) { // Spieler 1 wählt 'O'
            symbolSpieler1 = 'O';
            starterDerRunde = 'O';
        } else { // Spieler 1 wählt 'X' (oder schließt Fenster)
            symbolSpieler1 = 'X';
            starterDerRunde = 'X';
        }
        aktuellerSpieler = starterDerRunde;

        logik = new Spielfeld();
        buttons = new JButton[3][3];

        // Fenster-Setup
        setTitle("Tic Tac Toe");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header (Punkte & Status)
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        punkteLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(punkteLabel);
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Spielfeld
        JPanel spielfeldPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 80));
                buttons[i][j].setFocusable(false);
                int z = i, s = j;
                buttons[i][j].addActionListener(e -> buttonGeklickt(z, s));
                spielfeldPanel.add(buttons[i][j]);
            }
        }
        add(spielfeldPanel, BorderLayout.CENTER);

        aktualisiereAnzeige();
        setVisible(true);
    }

    // Wird aufgerufen, wenn ein Button im Spielfeld geklickt wird.
    // Verarbeitet den Zug, prüft auf Sieg/Unentschieden und wechselt den Spieler.
    private void buttonGeklickt(int z, int s) {
        if (!spielAktiv || !logik.feldFreiDirekt(z, s)) return;

        logik.setzeZeichenDirekt(z, s, aktuellerSpieler);
        zuege++;
        buttons[z][s].setText(String.valueOf(aktuellerSpieler));
        buttons[z][s].setForeground(aktuellerSpieler == 'X' ? Color.BLUE : Color.RED);

        if (logik.pruefeSieg(aktuellerSpieler)) {
            spielAktiv = false;
            // Wer bekommt den Punkt?
            if (aktuellerSpieler == symbolSpieler1) {
                siegeSpieler1++;
            } else {
                siegeSpieler2++;
            }
            // Verlierer fängt nächste Runde an
            starterDerRunde = (aktuellerSpieler == 'X') ? 'O' : 'X';

            aktualisiereAnzeige();
            starteGewinnAnimation(logik.getGewinnZeilen(aktuellerSpieler));
            return;
        }

        if (zuege == 9) {
            spielAktiv = false;
            starterDerRunde = (starterDerRunde == 'X') ? 'O' : 'X';
            aktualisiereAnzeige();
            JOptionPane.showMessageDialog(this, "Unentschieden!");
            spielZuruecksetzen();
            return;
        }

        aktuellerSpieler = (aktuellerSpieler == 'X') ? 'O' : 'X';
        aktualisiereAnzeige();
    }

    // Aktualisiert die Texte und Farben im Header (Punktestand und Statusmeldung).
    private void aktualisiereAnzeige() {
        punkteLabel.setText(name1 + ": " + siegeSpieler1 + " | " + name2 + ": " + siegeSpieler2);
        String name = (aktuellerSpieler == symbolSpieler1) ? name1 : name2;
        statusLabel.setText(name + " (" + aktuellerSpieler + ") ist am Zug");
        statusLabel.setForeground(aktuellerSpieler == 'X' ? Color.BLUE : Color.RED);
    }

    // Lässt die Gewinnerfelder blinken und öffnet danach den Dialog für eine neue Runde.
    private void starteGewinnAnimation(int[][] felder) {
        Timer timer = new Timer(300, null);
        final int[] blinken = {0};
        timer.addActionListener(e -> {
            Color c = (blinken[0] % 2 == 0) ? Color.BLACK : null;
            for (int[] f : felder) buttons[f[0]][f[1]].setBackground(c);
            if (++blinken[0] > 6) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Runde beendet!");
                spielZuruecksetzen();
            }
        });
        timer.start();
    }

    // Startet den Dialog für eine neue Runde oder zeigt das Endergebnis an.
    private void spielZuruecksetzen() {
        int w = JOptionPane.showConfirmDialog(this, "Noch eine Runde?", "Revanche?", JOptionPane.YES_NO_OPTION);
        if (w == JOptionPane.YES_OPTION) {
            logik = new Spielfeld();
            zuege = 0;
            aktuellerSpieler = starterDerRunde;
            spielAktiv = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground(null);
                }
            }
            aktualisiereAnzeige();
        } else {
            // --- HIER DIE VERABSCHIEDUNG ---
            String siegerNachPunkten;
            if (siegeSpieler1 > siegeSpieler2) {
                siegerNachPunkten = "\nGesamtsieger ist: " + name1 + "! 🏆";
            } else if (siegeSpieler2 > siegeSpieler1) {
                siegerNachPunkten = "\nGesamtsieger ist: " + name2 + "! 🏆";
            } else {
                siegerNachPunkten = "\nDas Spiel endet unentschieden! 🤝";
            }

            JOptionPane.showMessageDialog(this,
                    "Vielen Dank fürs Spielen!\n\n" +
                            "ENDERGEBNIS:\n" +
                            name1 + ": " + siegeSpieler1 + " Siege\n" +
                            name2 + ": " + siegeSpieler2 + " Siege\n" +
                            siegerNachPunkten,
                    "Spiel beendet",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    // Startpunkt des Programms.
    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
