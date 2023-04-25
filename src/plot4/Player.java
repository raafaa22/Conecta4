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
 * Clase abstracta Player para representar al jugador CPU
 *
 *
 */
public abstract class Player {

    /**
     * Devuelve una columna al azar siempre que no esté completa
     *
     * @param tablero Representación del tablero de juego
     * @return posición donde dejar caer la ficha
     */
    protected final int getRandomColumn(Grid tablero) {
        int posicion;

        // Buscar columna en la que se pueda poner la ficha
        do {
            posicion = (int) (Math.random() * tablero.getColumnas());
        } while (tablero.fullColumn(posicion));

        return posicion;
    } // getRandomColumn

    /**
     * Método abstracto para colocar una ficha en el tablero
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return columna donde dejar caer la ficha
     */
    public abstract int turno(Grid tablero, int conecta);

} // Player
