package com.reversedots.enums;

//Enum que representa los posibles resultados de una jugada o un turno de juego(facilita comunicacion entre modelo y vista)
public enum GameResult {
    SUCCESS,
    INVALID_MOVE,
    TURN_SKIPPED,
    GAME_OVER
}