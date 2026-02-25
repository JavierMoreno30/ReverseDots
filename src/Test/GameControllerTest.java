package Test;

import com.reversedots.controller.GameController;
import com.reversedots.enums.GameResult;
import com.reversedots.model.Player;
import com.reversedots.model.PieceColor;
import com.reversedots.repository.GameRepository;
import com.reversedots.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private GameController gameController;
    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        playerRepository = Mockito.mock(PlayerRepository.class);
        gameRepository = Mockito.mock(GameRepository.class);
        gameController = new GameController(playerRepository, gameRepository);
        player1 = new Player("Jugador 1");
        player2 = new Player("Jugador 2");
    }

    @Test
    void test1_InitNewGameSetsUpCorrectly() {
        // Este test verifica que el juego se inicia correctamente. Debería pasar sin problemas.
        gameController.initNewGame(8, player1, player2);

        assertEquals(8, gameController.getBoardSize());
        assertEquals(PieceColor.BLACK, gameController.getCurrentTurn(), "El primer turno siempre debe ser de las NEGRAS.");
        verify(playerRepository, times(2)).save(any(Player.class));
    }

    @Test
    void test2_MakeAValidMove_ShouldReturnSuccess() {
        // Este test prueba un movimiento que sabemos que es VÁLIDO.
        gameController.initNewGame(8, player1, player2);

        // El tablero inicial es:
        // (3,3)=W, (3,4)=B
        // (4,3)=B, (4,4)=W
        // El turno es de NEGRAS.
        // Un movimiento en (2,3) es VÁLIDO porque encierra la ficha BLANCA en (3,3)
        // usando la ficha NEGRA que ya está en (4,3).
        GameResult result = gameController.makeMove(2, 3);

        assertEquals(GameResult.SUCCESS, result, "El movimiento en (2,3) debería ser VÁLIDO, pero el controlador no lo considera así.");
    }

    @Test
    void test3_MakeMoveOnOccupiedCell_ShouldReturnInvalidMove() {
        // Este test prueba un movimiento en una celda que ya está OCUPADA.
        // Es la razón más simple para que un movimiento sea inválido.
        gameController.initNewGame(8, player1, player2);

        // La celda (3,3) está ocupada por una ficha BLANCA desde el inicio.
        GameResult result = gameController.makeMove(3, 3);

        assertEquals(GameResult.INVALID_MOVE, result, "Intentar mover a una celda ocupada debe ser INVÁLIDO.");
    }

    @Test
    void test4_MakeMoveThatFlipsNothing_ShouldReturnInvalidMove() {
        // Este test prueba un movimiento en una celda vacía que NO encierra fichas enemigas.
        gameController.initNewGame(8, player1, player2);

        // La celda (0,0) está vacía pero no es adyacente a ninguna ficha enemiga.
        GameResult result = gameController.makeMove(0, 0);

        assertEquals(GameResult.INVALID_MOVE, result, "Mover a una celda que no encierra fichas debe ser INVÁLIDO.");
    }

    @Test
    void test5_TurnShouldSwitchAfterAValidMove() {
        // Este test verifica que el turno cambia de jugador después de un movimiento VÁLIDO.
        gameController.initNewGame(8, player1, player2);

        // Confirmamos que el turno inicial es de las negras.
        assertEquals(PieceColor.BLACK, gameController.getCurrentTurn());

        // Hacemos el movimiento válido de la prueba 2.
        gameController.makeMove(2, 3);

        // El turno ahora debería ser de las blancas.
        assertEquals(PieceColor.WHITE, gameController.getCurrentTurn(), "El turno no cambió a BLANCO después de un movimiento válido.");
    }
}