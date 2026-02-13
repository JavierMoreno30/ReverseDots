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
    private boolean gameFinished = false;

    public int getBoardSize() { return board.getSize(); }

    public boolean isValidMove(int row, int col) {
        return board.isValidMove(row, col, currentTurn);
    }
    private void finalizeGameIfNeeded() {
        if (gameFinished) return;

        int black = board.getCount(PieceColor.BLACK);
        int white = board.getCount(PieceColor.WHITE);

        if (black > white) {
            playerBlack.incrementWins();
            playerWhite.incrementLosses();
        } else if (white > black) {
            playerWhite.incrementWins();
            playerBlack.incrementLosses();
        } // empate: no suma nada (si tu profe quiere, lo ajustamos)

        // Guardar cambios en archivo
        playerRepo.save(playerBlack);
        playerRepo.save(playerWhite);

        gameFinished = true;
    }

    public void loadGame(String path) throws Exception {
        GameData data = gameRepo.load(path);
        this.board = data.board;
        this.currentTurn = data.currentTurn;
        this.playerBlack = data.playerBlack;
        this.playerWhite = data.playerWhite;

        // Asegura que los jugadores existan en repo (por si se cargó una partida vieja)
        playerRepo.save(playerBlack);
        playerRepo.save(playerWhite);
        gameFinished = false;

    }

    public GameController(PlayerRepository playerRepo, GameRepository gameRepo) {
        this.playerRepo = playerRepo;
        this.gameRepo = gameRepo;
    }

    // Cambiado de startNewGame a initNewGame para coincidir con MainFrame
    public void initNewGame(int size, Player black, Player white) {
        this.board = new Board(size);
        this.currentTurn = PieceColor.BLACK; // Regla 4: Negro inicia
        this.playerBlack = black;
        this.playerWhite = white;

        playerRepo.save(black);
        playerRepo.save(white);
        gameFinished = false;

    }

    public GameResult makeMove(int row, int col) {
        if (!board.isValidMove(row, col, currentTurn)) {
            return GameResult.INVALID_MOVE;
        }

        board.executeMove(row, col, currentTurn);
        switchTurn();

        if (isGameOver()) {
            finalizeGameIfNeeded();
            return GameResult.GAME_OVER;
        }

        if (!board.hasValidMoves(currentTurn)) {
            switchTurn(); // Regla 9: Salto de turno
            return GameResult.TURN_SKIPPED;
        }

        return GameResult.SUCCESS;
    }

    private void switchTurn() {
        currentTurn = (currentTurn == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private boolean isGameOver() {
        return board.isFull() || (!board.hasValidMoves(PieceColor.BLACK) && !board.hasValidMoves(PieceColor.WHITE));
    }

    public Board getBoard() { return board; }
    public PieceColor getCurrentTurn() { return currentTurn; }

    public String getWinnerMessage() {
        int black = board.getCount(PieceColor.BLACK);
        int white = board.getCount(PieceColor.WHITE);
        if (black > white) return "Ganador: " + playerBlack.getName();
        if (white > black) return "Ganador: " + playerWhite.getName();
        return "Empate técnico";
    }

    public String getScoreBoard() {
        return String.format("%s: %d | %s: %d",
                playerBlack.getName(), board.getCount(PieceColor.BLACK),
                playerWhite.getName(), board.getCount(PieceColor.WHITE));
    }

    public void saveCurrentGame(String path) throws Exception {
        GameData data = new GameData();
        data.board = this.board;
        data.currentTurn = this.currentTurn;
        data.playerBlack = this.playerBlack;
        data.playerWhite = this.playerWhite;
        gameRepo.save(data, path);
    }
}