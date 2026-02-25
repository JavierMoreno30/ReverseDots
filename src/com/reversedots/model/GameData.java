package com.reversedots.model;

import java.io.Serializable;

//contendor de datos para guardar el estado del juego
public class GameData implements Serializable {
    public Board board;
    public PieceColor currentTurn;
    public Player playerBlack;
    public Player playerWhite;
}
