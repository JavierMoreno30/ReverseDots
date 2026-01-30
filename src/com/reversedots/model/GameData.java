package com.reversedots.model;

import java.io.Serializable;

/**
 * Contenedor para agrupar todos los datos necesarios para persistir una partida.
 */
public class GameData implements Serializable {
    public Board board;
    public PieceColor currentTurn;
    public Player playerBlack;
    public Player playerWhite;
}
