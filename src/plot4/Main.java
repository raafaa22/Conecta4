/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Curso 2016-17: Primera versión, Conecta-N Curso 2017-18: Se introducen
 * obstáculos aleatorios Curso 2018-19: Conecta-4 Curso 2021-22: Conecta4
 * clásico Curso 2022-23: Plot4 revisado
 *
 * Última revisión: 30-03-2023
 *
 * Código original: * Lenin Palafox * http://www.leninpalafox.com
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements ActionListener {

    // Número de turnos/movimientos
    private int movimiento = 0;
    // Turno (empieza jugador 1)
    private boolean turnoJ1 = true;
    // Jugador 2, CPU por defecto
    private boolean jugadorcpu = true;
    // Jugador 2, CPU aleatorio por defecto
    private int iaplayer;
    // Marca si el jugador pulsa sobre el tablero
    private boolean pulsado;

    // Jugadores (tipos de fichas en el tablero)
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = -1;
    public static final int VACIO = 0;

    // Algoritmos
    public static final int MINIMAX = 1;
    public static final int MINIMAXRES = 2;
    public static final int ALFABETA = 3;

    // Parámetros
    // Número de filas
    private static final int FILAS = 6;
    // Número de columnas
    private static final int COLUMNAS = 7;
    // Número de fichas que han de alinearse para ganar
    private static final int CONECTA = 4;
    
    // Grid de juego
    private JButton[][] tableroGUI; // Tablero gráfico
    private Grid juego; // Tablero interno
    // Jugador CPU
    private Player player2;
    // Iconos
    private ImageIcon ficha1;
    private ImageIcon ficha2;

    // Menús y elementos de la GUI
    private final JMenuBar barra = new JMenuBar();
    private final JMenu archivo = new JMenu("Archivo");
    private final JMenu opciones = new JMenu("Opciones");
    private final JMenuItem salir = new JMenuItem("Salir");
    private final JRadioButton p1h = new JRadioButton("Humano", true);
    private final JRadioButton p2h = new JRadioButton("Humano", false);
    private final JRadioButton p2c = new JRadioButton("CPU (Greedy)", true);
    private final JRadioButton p2c2 = new JRadioButton("CPU (MiniMax)", false);
    private final JRadioButton p2c3 = new JRadioButton("CPU (MiniMax Restringido)", false);
    private final JRadioButton p2c4 = new JRadioButton("CPU (MiniMax AlfaBeta)", false);
    // Leyendas y cabeceras
    private String cabecera = "Pr\u00e1cticas de IA (Curso 2022-23)";
    private final JLabel nombre = new JLabel(cabecera, JLabel.CENTER);
    private final String title = "Plot4 - Pr\u00e1cticas de IA (Curso 2022-23)";

    /**
     * Gestión de eventos y del transcurso de la partida
     *
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Eventos del menú Opciones
        if (ae.getSource() == p2h) {
            jugadorcpu = false; // Humano
            reset(); // El juego comienza de nuevo
        } else if (ae.getSource() == p2c) {
            jugadorcpu = true; // CPU Greedy
            iaplayer = 0;
            reset(); // El juego comienza de nuevo
        } else if (ae.getSource() == p2c2) {
            jugadorcpu = true; // CPU MiniMax;
            iaplayer = MINIMAX;
            reset(); // El juego comienza de nuevo
        } else if (ae.getSource() == p2c3) {
            jugadorcpu = true; // CPU MiniMax Restringido;
            iaplayer = MINIMAXRES;
            reset(); // El juego comienza de nuevo
        } else if (ae.getSource() == p2c4) {
            jugadorcpu = true; // CPU MiniMax AlfaBeta;
            iaplayer = ALFABETA;
            reset(); // El juego comienza de nuevo
        } else if (ae.getSource() == salir) {
            dispose();
            System.exit(0); // Terminar y salir
        } else {
            // Control del juego por el usuario
            int x;
            // Siempre empieza el jugador 1
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if (ae.getSource() == tableroGUI[i][j]) {
                        x = turnoJ1 ? juego.set(j, PLAYER1) : juego.set(j, PLAYER2);
                        // Comprobar si la jugada es válida
                        if (!(x < 0)) {
                            //Si es modo un jugador o dos
                            if (jugadorcpu) {
                                pulsado = true;
                            }
                            turnoJ1 = !turnoJ1;
                            movimiento++;
                            // Comprobar si acabó el juego
                            finJuego(juego.checkWin());
                        } // En otro caso, la columna ya está completa
                    } // if
                } // for 2
            } // for 1

            // Pasa el turno al jugador 2 (cuando se juega contra la CPU)
            if (pulsado) {
                if (jugadorcpu) {
                    pulsado = false;
                    turnoJ1 = !turnoJ1;
                    movimiento++;
                    // El jugador CPU hace su jugada
                    int pos = player2.turno(juego, CONECTA);
                    juego.set(pos, PLAYER2);
                    // Comprobar si acabó el juego
                    finJuego(juego.checkWin());
                }
            }

            // Actualizar cabecera
            String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
            cabecera2 += turnoJ1 ? "Jugador 1" : "Jugador2 ";
            nombre.setText(cabecera2);
        }

    } // actionPerformed         

    // Método para actualizar el tablero por pantalla
    private void updateGrid() {
        System.out.println("Paso: " + movimiento);
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(juego.get(i, j) + " ");
                switch (juego.get(i, j)) {
                    case PLAYER1:
                        tableroGUI[i][j].setIcon(ficha1);
                        break;
                    case PLAYER2:
                        tableroGUI[i][j].setIcon(ficha2);
                        break;
                    default:
                        tableroGUI[i][j].setIcon(null);
                }
            }
            System.out.println();
        }
        System.out.println();
    } // repaint

    // Terminar el juego en caso de finalizar la partida
    public void finJuego(int ganador) {

        // Actualizar tablero tras cada movimiento
        updateGrid();
        // Mostrar mensaje si hay ganador
        switch (ganador) {
            case PLAYER1:
                System.out.println("Ganador: Jugador 1, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 1\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, ficha1);
                reset();
                break;
            case PLAYER2:
                System.out.println("Ganador: Jugador 2, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 2\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, ficha2);
                reset();
                break;
            default:
                // Comprobamos si llegamos al final del juego
                if (movimiento >= FILAS * COLUMNAS) {
                    // Empate!!!
                    JOptionPane.showMessageDialog(this, "¡Empate!", "Conecta4", JOptionPane.INFORMATION_MESSAGE);
                    reset();
                }
                break;
        }

    } // finJuego

    /**
     * Reinicia una partida
     */
    private void reset() {
        // Volver el programa al estado inicial	
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tableroGUI[i][j].setIcon(null);
                juego.getGrid()[i][j] = VACIO;
            }
        }
        turnoJ1 = true; // Empieza a jugar el Jugador 1 (Humano)
        movimiento = 0;
        pulsado = false;

        System.out.println();
        System.out.print("Nueva partida. Jugador 1: Humano vs Jugador 2: ");
        if (jugadorcpu) {
            switch (iaplayer) {
                case MINIMAX:
                    player2 = new MiniMaxPlayer();
                    System.out.println("CPU (MiniMax)");
                    cabecera = "Juego: Humano vs CPU MiniMax - ";
                    break;
                case MINIMAXRES:
                    player2 = new MiniMaxRestrainedPlayer();
                    System.out.println("CPU (MiniMax Restringido)");
                    cabecera = "Juego: Humano vs CPU MiniMax Restringido - ";
                    break;
                case ALFABETA:
                    player2 = new AlfaBetaPlayer();
                    System.out.println("CPU (MiniMax AlfaBeta)");
                    cabecera = "Juego: Humano vs CPU MiniMax AlfaBeta - ";
                    break;
                default:
                    player2 = new GreedyPlayer();
                    System.out.println("CPU (Greedy)");
                    cabecera = "Juego: Humano vs CPU Greedy - ";
            } // case
        } else {
            player2 = null;
            System.out.println("Humano");
            cabecera = "Juego: Humano vs Humano - ";
        }
        // Refrescar tablero por pantalla
        updateGrid();
        // Actualizar cabecera
        String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
        cabecera2 += turnoJ1 ? "Jugador 1" : "Jugador2 ";
        nombre.setText(cabecera2);

    } // reset

    // Inicialización de un tablero vacío (GUI)
    private void initialize(int i, int j, JButton[][] tablero, Color col) {
        tablero[i][j] = new JButton();
        tablero[i][j].addActionListener(this);
        tablero[i][j].setBackground(col);
    }

    /**
     * Configuración inicial
     *
     * Creación de la interfaz gráfica del juego
     */
    private void run() {
        // Estado inicial del tablero
        juego = new Grid(FILAS, COLUMNAS, CONECTA);
        tableroGUI = new JButton[FILAS][COLUMNAS];
        //Cargar imagenes
        ficha1 = new ImageIcon("assets/player1.png");
        ficha2 = new ImageIcon("assets/player2.png");
        int altoVentana = (FILAS + 1) * ficha1.getIconWidth();
        int anchoVentana = COLUMNAS * ficha2.getIconWidth();

        switch (iaplayer) {
            case MINIMAX:
                player2 = new MiniMaxPlayer();
                break;
            case MINIMAXRES:
                player2 = new MiniMaxRestrainedPlayer();
                break;
            case ALFABETA:
                player2 = new AlfaBetaPlayer();
                break;
            default:
                player2 = new GreedyPlayer();
        } // case

        // Menú GUI
        salir.addActionListener(this);
        archivo.add(salir);
        // Player 1
        opciones.add(new JLabel("Jugador 1:"));
        ButtonGroup m1Jugador = new ButtonGroup();
        m1Jugador.add(p1h);
        // Player 2
        opciones.add(p1h);
        opciones.add(new JLabel("Jugador 2:"));
        p2h.addActionListener(this);
        p2c.addActionListener(this);
        p2c2.addActionListener(this);
        p2c3.addActionListener(this);
        p2c4.addActionListener(this);
        ButtonGroup m2Jugador = new ButtonGroup();
        m2Jugador.add(p2h);
        m2Jugador.add(p2c);
        m2Jugador.add(p2c2);
        m2Jugador.add(p2c3);
        m2Jugador.add(p2c4);
        opciones.add(p2h);
        opciones.add(p2c);
        opciones.add(p2c2);
        opciones.add(p2c3);
        opciones.add(p2c4);

        barra.add(archivo);
        barra.add(opciones);
        setJMenuBar(barra);

        // Panel Principal 
        JPanel principal = new JPanel();
        principal.setLayout(new GridLayout(FILAS, COLUMNAS));

        // Colocar Botones
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                initialize(i, j, tableroGUI, Color.BLACK);
                principal.add(tableroGUI[i][j]);
            }
        }
        nombre.setForeground(Color.BLUE);
        add(nombre, "North");
        add(principal, "Center");

        // Para cerrar la Ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });

        // Tamaño y localización del frame
        setLocation(170, 25);
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        setTitle(title);
        setVisible(true);
        reset();
    } // run

    /**
     * Método principal
     *
     * Lectura de parámetros desde línea de comandos e inicio del programa
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Plot4 - 4 en Raya");
        System.out.println("-----------------------------------------");
        System.out.println("Inteligencia Artificial - Curso 2022-23");

        Main juego = new Main();
        juego.run();

    } // main

} // Main
