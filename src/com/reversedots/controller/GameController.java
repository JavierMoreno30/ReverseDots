package com.reversedots.controller;

import com.reversedots.model.Board;
import com.reversedots.enums.GameResult;
import com.reversedots.model.GameData;
import com.reversedots.model.PieceColor;
import com.reversedots.model.Player;
import com.reversedots.repository.GameRepository;
import com.reversedots.repository.PlayerRepository;

//Controller principal que maneja la logica del juego, interacción con el modelo y persistencia.Este es el puente entre la vista (MainFrame) y el modelo (Board, Player).
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
        } //en este caso seria un empate tecnico, entonces no se suma nada a las estadisticas de los jugadores

        //se guardan los cambios en el repositorio de jugadores para actualizar las estadísticas
        playerRepo.save(playerBlack);
        playerRepo.save(playerWhite);

        gameFinished = true;
    }
    //metodo para cargar una partida desde un archivo, se encarga de restaurar el estado del juego y asegurar que los jugadores existan en el repositorio
    public void loadGame(String path) throws Exception {
        GameData data = gameRepo.load(path);
        this.board = data.board;
        this.currentTurn = data.currentTurn;
        this.playerBlack = data.playerBlack;
        this.playerWhite = data.playerWhite;

        //asegura que los jugadores existan en repo (por si se cargó una partida vieja)
        playerRepo.save(playerBlack);
        playerRepo.save(playerWhite);
        gameFinished = false;

    }
    //constructor que recibe los repositorios necesarios para la persistencia de jugadores y partidas.
    public GameController(PlayerRepository playerRepo, GameRepository gameRepo) {
        this.playerRepo = playerRepo;
        this.gameRepo = gameRepo;
    }

    //inicializa una nueva partida con un tablero del tamaño especificado y asigna los jugadores. establece el turno inicial
    public void initNewGame(int size, Player black, Player white) {
        this.board = new Board(size);
        this.currentTurn = PieceColor.BLACK; //regla 4: Negro inicia
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
            switchTurn(); //regla 9: Salto de turno
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
//getters para que la vista pueda acceder al estado actual del juego
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
//metodo para guardar el estado actual del juego en un archivo, empaquetando la iformacion en un objeto GameData y dandole persistencia al GameRepository
    public void saveCurrentGame(String path) throws Exception {
        GameData data = new GameData();
        data.board = this.board;
        data.currentTurn = this.currentTurn;
        data.playerBlack = this.playerBlack;
        data.playerWhite = this.playerWhite;
        gameRepo.save(data, path);
    }
}