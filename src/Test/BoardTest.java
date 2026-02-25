package Test;

import com.reversedots.model.Board;
import com.reversedots.model.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    //se ejecuta antes de cada prueba para asegurar que cada prueba empiece con tablero limpio
    @BeforeEach
    void setUp() {
        board = new Board(8);
    }

    @Test
    void test1_BoardInitialization() {
        //prueba que el tablero se crea con el tamaño correcto y las fichas iniciales
        int mid = board.getSize() / 2;
        assertEquals(8, board.getSize(), "El tamaño del tablero debe ser 8.");

        //verifica la posición de las 4 fichas iniciales.
        assertEquals(PieceColor.WHITE, board.getCell(mid - 1, mid - 1), "Debe haber una ficha blanca en la posición inicial.");
        assertEquals(PieceColor.WHITE, board.getCell(mid, mid), "Debe haber una ficha blanca en la posición inicial.");
        assertEquals(PieceColor.BLACK, board.getCell(mid - 1, mid), "Debe haber una ficha negra en la posición inicial.");
        assertEquals(PieceColor.BLACK, board.getCell(mid, mid - 1), "Debe haber una ficha negra en la posición inicial.");
    }

    @Test
    void test2_InvalidBoardSizeThrowsException() {

        assertThrows(IllegalArgumentException.class, () -> new Board(5), "Un tamaño impar debería lanzar una excepción.");

        //2 (no valido)
        assertThrows(IllegalArgumentException.class, () -> new Board(2), "Un tamaño menor a 4 debería lanzar una excepción.");
    }

    @Test
    void test3_InitialValidMovesForBlack() {
        //el jugador negro tiene movimientos válidos
        assertTrue(board.isValidMove(2, 3, PieceColor.BLACK), "Debería ser un movimiento válido para las negras.");
        assertTrue(board.isValidMove(3, 2, PieceColor.BLACK), "Debería ser un movimiento válido para las negras.");
        assertFalse(board.isValidMove(0, 0, PieceColor.BLACK), "Una esquina no debería ser un movimiento válido al inicio.");
    }

    @Test
    void test4_ExecuteMoveAndFlipOpponent() {

        board.executeMove(2, 3, PieceColor.BLACK);

        //nueva ficha está en su lugar
        assertEquals(PieceColor.BLACK, board.getCell(2, 3), "La nueva ficha negra debe estar en (2,3).");

        //ficha blanca en (3, 3) fue volteada a negra
        assertEquals(PieceColor.BLACK, board.getCell(3, 3), "La ficha del oponente en (3,3) debe ser volteada a negra.");
    }

    @Test
    void test5_InitialPieceCount() {
        //debe haber 2 fichas de cada color.
        assertEquals(2, board.getCount(PieceColor.BLACK), "El conteo inicial de fichas negras debe ser 2.");
        assertEquals(2, board.getCount(PieceColor.WHITE), "El conteo inicial de fichas blancas debe ser 2.");

        //resto vacio
        int totalCells = board.getSize() * board.getSize();
        assertEquals(totalCells - 4, board.getCount(PieceColor.EMPTY), "El resto de las celdas deben estar vacías.");
    }
}