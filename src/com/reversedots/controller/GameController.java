package com.reversedots.controller;

import com.reversedots.model.Board;
import com.reversedots.enums.GameResult;
import com.reversedots.model.GameData;
import com.reversedots.model.PieceColor;
import com.reversedots.model.Player;
import com.reversedots.repository.GameRepository;
import com.reversedots.repository.PlayerRepository;

public class GameController {
    private Board board;
    private PieceColor currentTurn;
    private Player playerBlack;
    private Player playerWhite;

    private final PlayerRepository playerRepo;
    private final GameRepository gameRepo;

    public GameController(PlayerRepository playerRepo, GameRepository gameRepo) {
        this.playerRepo = playerRepo;
        this.gameRepo = gameRepo;
    }

    /**
     * Intenta realizar un movimiento y actualiza el estado del juego.
     */
    public GameResult makeMove(int row, int col) {
        //validar si el movimiento es legal
        if (!board.isValidMove(row, col, currentTurn)) {
            return GameResult.INVALID_MOVE;
        }
        //ejecutar el movimiento
        board.executeMove(row, col, currentTurn);

        //cambiar el turno
        switchTurn();
        //verificar si el juego terminó o si el siguiente jugador puede mover
        if (isGameOver()) {
            return GameResult.GAME_OVER;
        }
        if (!hasValidMoves(currentTurn)) {
            switchTurn(); //el oponente pierde el turno (Regla 9)
            return GameResult.TURN_SKIPPED;
        }
        return GameResult.SUCCESS;
    }

    private void switchTurn() {
        currentTurn = (currentTurn == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private boolean isGameOver() {
        //el juego termina si el tablero está lleno o si NADIE puede mover (Regla 10)
        return board.isFull() || (!board.hasValidMoves(PieceColor.BLACK) && !board.hasValidMoves(PieceColor.WHITE));
    }

    private boolean hasValidMoves(PieceColor color) {
        //el controlador le pregunta al tablero si ese color tiene jugadas posibles
        return board.hasValidMoves(color);
    }

    public void startNewGame(int size, Player black, Player white) {
        this.board = new Board(size);
        this.currentTurn = PieceColor.BLACK; //negro siempre inicia(Regla 4)
        this.playerBlack = black;
        this.playerWhite = white;

        // Registrar jugadores si no existen
        playerRepo.save(black);
        playerRepo.save(white);
    }

    //Método exigido: Guardar partida
    public void saveCurrentGame(String path) throws Exception {
        GameData data = new GameData();
        data.board = this.board;
        data.currentTurn = this.currentTurn;
        data.playerBlack = this.playerBlack;
        data.playerWhite = this.playerWhite;
        gameRepo.save(data, path);
    }

    //Método para obtener el ganador al finalizar
    public String getWinnerMessage() {
        int black = board.getCount(PieceColor.BLACK);
        int white = board.getCount(PieceColor.WHITE);

        if (black > white) return "Ganador: " + playerBlack.getName();
        if (white > black) return "Ganador: " + playerWhite.getName();
        return "Empate";
    }

    public String getScoreBoard() {
        return String.format("Marcador - %s (Negras): %d | %s (Blancas): %d",
                playerBlack.getName(), board.getCount(PieceColor.BLACK),
                playerWhite.getName(), board.getCount(PieceColor.WHITE));
    }
}