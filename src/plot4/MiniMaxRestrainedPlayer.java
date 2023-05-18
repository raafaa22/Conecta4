/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxRestrainedPlayer para representar al jugador CPU que usa una
 * técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar el algoritmo
 * MiniMax Restringido
 *
 */
public class MiniMaxRestrainedPlayer extends Player {

    private static final int PLAYER = 1; // Jugador humano
    private static final int AI = -1; // IA

    private static final int numFilas = Main.FILAS; // Numero de filas del tablero
    private static final int numColumnas = Main.COLUMNAS; // Numero de columnas del tablero
    private static final int conectar = Main.CONECTA; // Numero de fichas consecutivas necesarias para ganar

    @Override
    public int turno(Grid tablero, int conecta) {
        int mejorMovimiento = -1;
        int mejorValor = Integer.MIN_VALUE;

        // Obtener los movimientos disponibles
        ArrayList<Integer> movimientos = columnasLibres(tablero);

        // Para cada movimiento disponible
        for (int movimiento : movimientos) {
            // Copiar el tablero actual
            Grid nuevoTablero = new Grid(tablero);
            // Realizar el movimiento en el nuevo tablero
            nuevoTablero.set(movimiento, AI);
            int nivelMaximo = 6; // Establecer el nivel máximo deseado
            int valorMovimiento = minimax(nuevoTablero, false, 0, nivelMaximo);
            // Actualizar el mejor valor y movimiento si corresponde
            if (valorMovimiento > mejorValor) {
                mejorValor = valorMovimiento;
                mejorMovimiento = movimiento;
            }
        }

        return mejorMovimiento;
    }

    private ArrayList<Integer> columnasLibres(Grid tablero) {
        ArrayList<Integer> movimientos = new ArrayList<>();

        // Recorrer las columnas del tablero
        for (int columna = 0; columna < numColumnas; columna++) {
            // Si la columna no está llena, añadir el movimiento a la lista
            if (!tablero.fullColumn(columna)) {
                movimientos.add(columna);
            }
        }

        return movimientos;
    }


    public boolean tableroLleno(Grid tablero) {
        for (int columna = 0; columna < numColumnas; columna++) {
            if (!tablero.fullColumn(columna)) {
                return false;
            }
        }
        return true;
    }

    private int minimax(Grid tablero, boolean esMaximizador, int nivel, int nivelMaximo) {
        // Comprobar si el juego ha terminado
        int resultado = tablero.checkWin();
        if (resultado == AI) {
            // La IA ha ganado
            return 1;
        } else if (resultado == PLAYER) {
            // El jugador humano ha ganado
            return -1;
        } else if (tableroLleno(tablero)) {
            // El tablero está lleno y es un empate
            return 0;
        } else if (nivel == nivelMaximo) {
            // Se ha alcanzado el nivel preestablecido, asignar valor heurístico
            return evaluarEstado(tablero);
        }

        if (esMaximizador) {
            int mejorValor = Integer.MIN_VALUE;
            // Obtener los movimientos disponibles
            ArrayList<Integer> movimientos = columnasLibres(tablero);
            // Para cada movimiento disponible
            for (int movimiento : movimientos) {
                // Copiar el tablero actual
                Grid nuevoTablero = new Grid(tablero);
                // Realizar el movimiento en el nuevo tablero
                nuevoTablero.set(movimiento, AI);
                // Llamar recursivamente al algoritmo MiniMax para el siguiente nivel
                int valorMovimiento = minimax(nuevoTablero, false, nivel + 1, nivelMaximo);
                // Actualizar el mejor valor si es mayor
                mejorValor = Math.max(mejorValor, valorMovimiento);
            }
            System.out.println("El mejor valor es: " + mejorValor);
            return mejorValor;
        } else {
            int peorValor = Integer.MAX_VALUE;
            // Obtener los movimientos disponibles
            ArrayList<Integer> movimientos = columnasLibres(tablero);
            // Para cada movimiento disponible
            for (int movimiento : movimientos) {
                // Copiar el tablero actual
                Grid nuevoTablero = new Grid(tablero);
                // Realizar el movimiento en el nuevo tablero
                nuevoTablero.set(movimiento, PLAYER);
                // Llamar recursivamente al algoritmo MiniMax para el siguiente nivel
                int valorMovimiento = minimax(nuevoTablero, true, nivel + 1, nivelMaximo);
                // Actualizar el peor valor si es menor
                peorValor = Math.min(peorValor, valorMovimiento);
            }

            System.out.println("El peor valor es: " + peorValor);

            return peorValor;
        }
    }
    private int evaluarEstado(Grid tablero) {
        int utilidad = 0;

        // Evaluación horizontal
        utilidad += evaluarLineas(tablero.copyGrid(), numFilas, numColumnas, conectar, AI);

        // Evaluación vertical
        int[][] tableroTranspuesto = transponer(tablero);
        utilidad += evaluarLineas(tableroTranspuesto, numColumnas, numFilas, conectar, AI);

        // Evaluación diagonales principales
        int numDiagonalesPrincipales = numFilas - conectar + 1;
        int numDiagonalesSecundarias = numColumnas - conectar + 1;
        for (int i = 0; i < numDiagonalesPrincipales; i++) {
            int[] diagonal = obtenerDiagonalPrincipal(tablero,i);
            utilidad += evaluarLinea(diagonal, conectar, AI);
        }

        // Evaluación diagonales secundarias
        for (int i = 0; i < numDiagonalesSecundarias; i++) {
            int[] diagonal = obtenerDiagonalSecundaria(tablero,i);
            utilidad += evaluarLinea(diagonal, conectar, AI);
        }

        return utilidad;
    }

    private int evaluarLineas(int[][] tablero, int filas, int columnas, int conectar, int jugador) {
        int utilidad = 0;

        // Evaluar todas las líneas en el tablero
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna <= columnas - conectar; columna++) {
                int[] linea = new int[conectar];
                System.arraycopy(tablero[fila], columna, linea, 0, conectar);
                utilidad += evaluarLinea(linea, conectar, jugador);
            }
        }

        return utilidad;
    }

    private int evaluarLinea(int[] linea, int conectar, int jugador) {
        int utilidad = 0;
        int contadorJugador = 0;
        int contadorOponente = 0;

        for (int i = 0; i < conectar; i++) {
            if (linea[i] == jugador) {
                contadorJugador++;
            } else if (linea[i] == -jugador) {
                contadorOponente++;
            }
        }

        if (contadorJugador > 0 && contadorOponente > 0) {
            // La línea contiene fichas de ambos jugadores, no es útil
            return 0;
        }

        if (contadorJugador > 0) {
            // El jugador tiene fichas consecutivas en la línea
            utilidad += Math.pow(contadorJugador, 2);
        } else if (contadorOponente > 0) {
            // El oponente tiene fichas consecutivas en la línea
            utilidad -= Math.pow(contadorOponente, 2);
        }

        return utilidad;
    }
    private int[] obtenerDiagonalSecundaria(Grid tablero, int i) {
        int[] diagonal = new int[numFilas];

        for (int fila = 0; fila < numFilas; fila++) {
            int columna = i - fila;
            if (columna >= 0 && columna < numColumnas) {
                diagonal[fila] = tablero.get(fila, columna);
            } else {
                diagonal[fila] = 0; // Espacio vacío fuera del tablero
            }
        }

        return diagonal;
    }

    private int[] obtenerDiagonalPrincipal(Grid tablero, int i) {
        int[] diagonal = new int[numFilas];

        for (int fila = 0; fila < numFilas; fila++) {
            int columna = i + fila;
            if (columna >= 0 && columna < numColumnas) {
                diagonal[fila] = tablero.get(fila, columna);
            } else {
                diagonal[fila] = 0; // Espacio vacío fuera del tablero
            }
        }

        return diagonal;
    }

    private int[][] transponer(Grid tablero) {
        int[][] transpuesto = new int[numColumnas][numFilas];

        for (int fila = 0; fila < numFilas; fila++) {
            for (int columna = 0; columna < numColumnas; columna++) {
                transpuesto[columna][fila] = tablero.get(fila, columna);
            }
        }

        return transpuesto;
    }


} // MiniMaxRestrainedPlayer
