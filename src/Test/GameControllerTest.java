package Test;

import com.reversedots.controller.GameController;
import com.reversedots.enums.GameResult;
import com.reversedots.model.GameData;
import com.reversedots.model.Player;
import com.reversedots.model.PieceColor;
import com.reversedots.repository.GameRepository;
import com.reversedots.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//clases "fakes", pero permiten que compilen los test
class FakePlayerRepository implements PlayerRepository {
    @Override public void save(Player player) { /* No hace nada */ }
    @Override public Player findByName(String name) { return null; }

    @Override
    public List<Player> findAll() {
        return List.of();
    }
}

class FakeGameRepository implements GameRepository {
    @Override public void save(GameData gameData, String filePath) { /* No hace nada */ }
    @Override public GameData load(String filePath) { return null; }
}

class GameControllerTest {

    private GameController gameController;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        //usamos nuestras clases manuales
        PlayerRepository playerRepository = new FakePlayerRepository();
        GameRepository gameRepository = new FakeGameRepository();

        gameController = new GameController(playerRepository, gameRepository);
        player1 = new Player("Jugador 1");
        player2 = new Player("Jugador 2");
    }

    @Test
    void test1_InitNewGameSetsUpCorrectly() {
        gameController.initNewGame(8, player1, player2);

        assertEquals(8, gameController.getBoardSize());
        assertEquals(PieceColor.BLACK, gameController.getCurrentTurn());
    }

    @Test
    void test2_MakeAValidMove_ShouldReturnSuccess() {
        gameController.initNewGame(8, player1, player2);

        //en el juego estándar 8x8, (2,3) es un movimiento válido inicial para negras
        GameResult result = gameController.makeMove(2, 3);

        assertEquals(GameResult.SUCCESS, result);
    }

    @Test
    void test3_MakeMoveOnOccupiedCell_ShouldReturnInvalidMove() {
        gameController.initNewGame(8, player1, player2);

        //la celda (3,3) ya está ocupada por la configuración inicial
        GameResult result = gameController.makeMove(3, 3);

        assertEquals(GameResult.INVALID_MOVE, result);
    }

    @Test
    void test4_TurnShouldSwitchAfterAValidMove() {
        gameController.initNewGame(8, player1, player2);

        //negro mueve
        gameController.makeMove(2, 3);

        //ahora debe ser blanco
        assertEquals(PieceColor.WHITE, gameController.getCurrentTurn());
    }
}