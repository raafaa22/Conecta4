package plot4;

import java.util.ArrayList;
import java.util.Iterator;

public class MiniMaxPlayer extends Player {

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
            // Calcular el valor del movimiento mediante el algoritmo MiniMax
            int valorMovimiento = minimax(nuevoTablero, false);
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

    private int minimax(Grid tablero, boolean esMaximizador) {
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
                int valorMovimiento = minimax(nuevoTablero, false);
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
                int valorMovimiento = minimax(nuevoTablero, true);
                // Actualizar el peor valor si es menor
                peorValor = Math.min(peorValor, valorMovimiento);
            }

            System.out.println("El peor valor es: " + peorValor);

            return peorValor;
        }
    }
}