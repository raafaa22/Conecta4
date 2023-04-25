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
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase GreedyPlayer para representar al jugador CPU que juega sobre la marcha
 *
 *
 */
public class GreedyPlayer extends Player {

    /**
     *
     * Método para determinar, de forma muy básica, donde colocar la siguiente ficha
     * No es especialmente brillante
     * 
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return columna donde dejar caer la ficha
     */
    @Override
    public int turno(Grid tablero, int conecta) {

        boolean cubrir_izquierda = false;
        int ganarVert = 0;
        int ganarHorz = 0;
        // Generar columna aleatoria por defecto
        int posicion;

        // Ataque en horizontal
        for (int i = 0; i < tablero.getFilas(); i++) {
            // Buscamos en todo el tablero
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.get(i,j) != Main.VACIO) {
                    if (tablero.get(i,j) == Main.PLAYER2) {
                        ganarHorz++;
                    } else {
                        ganarHorz = 0;
                    }
                    if (ganarHorz == conecta - 1) {
                        posicion = j;
                        if (posicion != tablero.getColumnas() - 1) {
                            if (!tablero.fullColumn(j + 1)) {
                                posicion++;
                            } else if (j >= conecta - 1 && !tablero.fullColumn(j - (conecta - 1)))
                                posicion = posicion - (conecta - 1);
                        }
                    }
                }
            }
            ganarHorz = 0;
        }
        // Defenderse en Horizontal hacia la izquierda
        ganarHorz = 0;
        for (int j = tablero.getColumnas() - 1; j >= 0; j--) {
            if (!tablero.fullColumn(j)) {
                if (tablero.topColumn(j) != Main.PLAYER2) {
                    ganarHorz++;
                } else {
                    ganarHorz = 0;
                }
                if (ganarHorz == conecta - 1 && j != 0) {
                    posicion = j;
                    if (!tablero.fullColumn(j - 1)) {
                        posicion--;
                        cubrir_izquierda = true;
                    }
                }
            }
        }
        // Defenderse en Horizontal hacia la derecha
        ganarHorz = 0;
        if (!cubrir_izquierda) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (!tablero.fullColumn(j)) {
                    if (tablero.topColumn(j) != Main.PLAYER2) {
                        ganarHorz++;
                    } else {
                        ganarHorz = 0;
                    }
                    if (ganarHorz == conecta - 1) {
                        posicion = j;
                        if (posicion != tablero.getColumnas() - 1) {
                            if (!tablero.fullColumn(j + 1))
                                posicion++;
                        }
                    }
                }
            }
        }
        // Defenderse en Vertical
        posicion = getRandomColumn(tablero);
        for (int i = 0; i < tablero.getFilas(); i++) {
            if (tablero.get(i, posicion) != Main.VACIO) {
                if (tablero.get(i, posicion) != Main.PLAYER2) {
                    ganarVert++;
                } else {
                    ganarVert = 0;
                }
                if (ganarVert != conecta - 1)
                    posicion = getRandomColumn(tablero); //obtiene la columna en la que se puede ganar;
            }
        }
        // Ataque en Vertical
        ganarVert = 0;
        for (int i = 0; i < tablero.getFilas(); i++) //buscamos en todo el tablero
        {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.get(i, j) != Main.VACIO) {
                    if (tablero.get(i, j) == Main.PLAYER2) {
                        ganarVert++;
                    } else {
                        ganarVert = 0;
                    }
                    if (ganarVert == conecta - 1 && j != 0) //si en alguna columna hay n-1 fichas seguidas de la mAquina
                        posicion = j; //obtiene la columna en la que se puede ganar;
                }
            }
            ganarVert = 0;
        }

        if (tablero.fullColumn(posicion)) // Si no se puede poner ficha en la columna (columna llena)
            posicion = getRandomColumn(tablero); // Genera posición aleatoria

        // Devolver posición
        return posicion;
    } // turno          

} // GreedyPlayer
