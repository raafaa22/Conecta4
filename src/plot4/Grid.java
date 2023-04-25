/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plot4;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen Última
 * revisión: 2023-04-14
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase Grid para representar el tablero de juego
 *
 *
 */
public class Grid {

    // Tablero de juego como array de enteros
    protected final int[][] tablero;
    // Número de filas
    protected final int filas;
    // Número de columnas
    protected final int columnas;
    // Número de fichas consecutivas para ganar
    protected final int conecta;

    // Constructor
    public Grid(int f, int c, int s) {
        filas = f;
        columnas = c;
        tablero = new int[filas][columnas];
        conecta = s;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = 0;
            }
        }
    } // Grid

    // Constructor 2 (copia)
    public Grid(Grid original) {
        filas = original.filas;
        columnas = original.columnas;
        tablero = new int[filas][columnas];
        conecta = original.conecta;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = original.tablero[i][j];
            }
        }
    } // Grid

    // Acceso a filas
    public int getFilas() {
        return filas;
    } // getFilas

    // Acceso a columnas
    public int getColumnas() {
        return columnas;
    } // getColumnas

    // Acceso al tablero
    public int[][] getGrid() {
        return tablero;
    } // getGrid

    // Devuelve una copia del tablero
    public int[][] copyGrid() {
        int[][] copia = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, columnas);
        }

        return copia;
    } // copyGrid

    // Método para comprobar si el estado actual del tablero es final y hay ganador
    // Devuelve:
    // 0: No hay ganador aún
    // Main.PLAYER1 (1) : Gana jugador 1
    // Main.PLAYER2 (-1) : Gana jugador 2
    public int checkWin() {

        int ganador = 0;
        int ganar1;
        int ganar2;
        boolean salir = false;
        // Comprobar horizontal
        for (int i = 0; (i < filas) && !salir; i++) {
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < columnas) && !salir; j++) {
                if (tablero[i][j] != Main.VACIO) {
                    if (tablero[i][j] == Main.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 >= conecta) {
                        ganador = Main.PLAYER1;
                        // Ganador 1 en horizontal;
                        salir = true;
                    }
                    if (!salir) {
                        if (tablero[i][j] == Main.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 >= conecta) {
                            ganador = Main.PLAYER2;
                            // Ganador 2 en horizontal;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
        }
        // Comprobar vertical
        for (int i = 0; (i < columnas) && !salir; i++) {
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < filas) && !salir; j++) {
                if (tablero[j][i] != Main.VACIO) {
                    if (tablero[j][i] == Main.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 >= conecta) {
                        ganador = Main.PLAYER1;
                        // Ganador 1 en vertical;
                        salir = true;
                    }
                    if (!salir) {
                        if (tablero[j][i] == Main.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 >= conecta) {
                            ganador = Main.PLAYER2;
                            // Ganador 2 en vertical;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
        }
        // Comprobar oblicuo. De izquierda a derecha
        for (int i = 0; i < filas && !salir; i++) {
            for (int j = 0; j < columnas && !salir; j++) {
                int a = i;
                int b = j;
                ganar1 = 0;
                ganar2 = 0;
                while (a < filas && b < columnas && !salir) {
                    if (tablero[a][b] != Main.VACIO) {
                        if (tablero[a][b] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        // Gana el jugador 1
                        if (ganar1 >= conecta) {
                            ganador = Main.PLAYER1;
                            // Ganador 1 en oblicuo izquierda;
                            salir = true;
                        }
                        if (ganador != Main.PLAYER1) {
                            if (tablero[a][b] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            // Gana el jugador 2
                            if (ganar2 >= conecta) {
                                ganador = Main.PLAYER2;
                                // Ganador 2 en oblicuo izquierda;
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                    a++;
                    b++;
                }
            }
        }
        // Comprobar oblicuo de derecha a izquierda 
        for (int i = filas - 1; i >= 0 && !salir; i--) {
            for (int j = 0; j < columnas && !salir; j++) {
                int a = i;
                int b = j;
                ganar1 = 0;
                ganar2 = 0;
                while (a >= 0 && b < columnas && !salir) {
                    if (tablero[a][b] != Main.VACIO) {
                        if (tablero[a][b] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        // Gana el jugador 1
                        if (ganar1 >= conecta) {
                            ganador = Main.PLAYER1;
                            // Ganador 1 en oblicuo derecha
                            salir = true;
                        }
                        if (ganador != Main.PLAYER1) {
                            if (tablero[a][b] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            // Gana el jugador 2
                            if (ganar2 >= conecta) {
                                ganador = Main.PLAYER2;
                                // Ganador 2 en oblicuo derecha
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                    a--;
                    b++;
                }
            }
        }

        return ganador;
    } // checkWin

    // Comprobar si una columna está completa
    public boolean fullColumn(int col) {
        int x = filas - 1;
        //Ir a la última posición de la columna	
        while ((x >= 0) && (tablero[x][col] != Main.VACIO)) {
            x--;
        }

        // Si x < 0, columna completa
        return (x < 0);

    } // fullColumn

    // Devuelve el color de la primera ficha de la columna
    // Devuelve:
    // Main.PLAYER1 (1) : jugador 1
    // Main.PLAYER2 (-1) : jugador 2
    public int topColumn(int col) {
        int x = filas - 1;
        //Ir a la última posición de la columna	
        while ((x >= 0) && (tablero[x][col] != Main.VACIO)) {
            x--;
        }

        if (x < 0) {
            return -2; // Error: La columna está completa
        } else {
            return tablero[x][col];
        }

    } // topColumn

    // Coloca una ficha en la columna col
    public int set(int col, int jugador) {

        int x = filas - 1;
        // Ir a la última posición de la columna	
        while ((x >= 0) && (tablero[x][col] != 0)) {
            x--;
        }

        // Si la columna no está llena, colocar la ficha
        if (x >= 0) {
            tablero[x][col] = jugador;
        }

        return x;

    } // set

    // Devuelve el valor en la celda (x,y)
    // Devuelve:
    // Main.VACIO (0) : Celda vacía
    // Main.PLAYER1 (1) : Celda con ficha del jugador 1
    // Main.PLAYER2 (-1) : Celda con ficha del jugador 2
    public int get(int x, int y) {
        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
            return tablero[x][y];
        } else // error: fuera de rango
        {
            return -2;
        }
    } // get

    // Nuevo
    // Imprime el tablero por pantalla
    // Método para mostrar el estado actual del tablero por la salida estándar
    public void print() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    } // print

    // Nuevo
    // Devuelve el número de fichas de un jugador ya colocadas en el tablero
    public int getCount(int player) {
        int count = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    } // getCount

} // Grid
