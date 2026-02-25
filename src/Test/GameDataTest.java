package Test;

import com.reversedots.model.Board;
import com.reversedots.model.GameData;
import com.reversedots.model.PieceColor;
import com.reversedots.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    private GameData gameData;
    private Board testBoard;
    private Player playerBlack;
    private Player playerWhite;

    @BeforeEach
    void setUp() {
        gameData = new GameData();
        testBoard = new Board(8);
        playerBlack = new Player("Jugador Negro");
        playerWhite = new Player("Jugador Blanco");
    }

    @Test
    void test1_GameDataShouldBeEmptyInitially() {
        //objeto GameData recién creado tiene todos sus campos nulos.
        assertNull(gameData.board, "El tablero debería ser nulo al inicio.");
        assertNull(gameData.currentTurn, "El turno actual debería ser nulo al inicio.");
        assertNull(gameData.playerBlack, "El jugador negro debería ser nulo al inicio.");
        assertNull(gameData.playerWhite, "El jugador blanco debería ser nulo al inicio.");
    }

    @Test
    void test2_SetAndGetBoard_ShouldWork() {
        //campo 'board' se puede asignar y recuperar
        gameData.board = testBoard;
        assertSame(testBoard, gameData.board, "El tablero asignado no es el mismo que se recuperó.");
    }

    @Test
    void test3_SetAndGetCurrentTurn_ShouldWork() {
        //campo 'currentTurn' se puede asignar y recuperar
        gameData.currentTurn = PieceColor.BLACK;
        assertEquals(PieceColor.BLACK, gameData.currentTurn, "El turno actual no se guardó correctamente.");
    }

    @Test
    void test4_SetAndGetPlayers_ShouldWork() {
        //campos de los jugadores se pueden asignar y recuperar
        gameData.playerBlack = playerBlack;
        gameData.playerWhite = playerWhite;

        assertSame(playerBlack, gameData.playerBlack, "El jugador negro no se guardó correctamente.");
        assertSame(playerWhite, gameData.playerWhite, "El jugador blanco no se guardó correctamente.");
    }

    @Test
    void test5_FullStateStorage_ShouldHoldAllData() {
        //test integral que verifica que todos los campos se guardan juntos
        gameData.board = testBoard;
        gameData.currentTurn = PieceColor.WHITE;
        gameData.playerBlack = playerBlack;
        gameData.playerWhite = playerWhite;

        //verificamos que cada pieza de datos es la que esperamos
        assertEquals(8, gameData.board.getSize());
        assertEquals(PieceColor.WHITE, gameData.currentTurn);
        assertEquals("Jugador Negro", gameData.playerBlack.getName());
        assertEquals("Jugador Blanco", gameData.playerWhite.getName());
    }
}