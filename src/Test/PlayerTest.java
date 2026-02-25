package Test;

import com.reversedots.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void test1_NewPlayer_ShouldHaveZeroStats() {
        //constructor básico crea un jugador con el nombre correcto y las estadísticas (victorias y derrotas) inicializadas en cero.
        Player player = new Player("Jugador Novato");

        assertAll("Un jugador nuevo debe tener nombre y estadísticas en cero",
                () -> assertEquals("Jugador Novato", player.getName(), "El nombre no se guardó correctamente."),
                () -> assertEquals(0, player.getGamesWon(), "Las victorias deberían ser 0 para un jugador nuevo."),
                () -> assertEquals(0, player.getGamesLost(), "Las derrotas deberían ser 0 para un jugador nuevo.")
        );
    }

    @Test
    void test2_LoadPlayerWithStats_ShouldStoreAllData() {
        //constructor completo carga correctamente el nombre las victorias y las derrotas.
        Player player = new Player("Jugador Veterano", 15, 7);

        assertAll("Un jugador cargado debe tener todas sus estadísticas correctas",
                () -> assertEquals("Jugador Veterano", player.getName()),
                () -> assertEquals(15, player.getGamesWon(), "Las victorias cargadas no son correctas."),
                () -> assertEquals(7, player.getGamesLost(), "Las derrotas cargadas no son correctas.")
        );
    }

    @Test
    void test3_IncrementWins_ShouldIncreaseGamesWonByOne() {
        //metodo incrementWins() aumneta de victoias en 1 y no afecta a las derrotas.
        Player player = new Player("Ganador", 5, 2);

        player.incrementWins(); //jugador gana una partida

        assertEquals(6, player.getGamesWon(), "El contador de victorias no se incrementó correctamente.");
        assertEquals(2, player.getGamesLost(), "El contador de derrotas no debería cambiar al ganar.");
    }

    @Test
    void test4_IncrementLosses_ShouldIncreaseGamesLostByOne() {
        //Asegura que el metodo incrementLosses() aumanta el contador de derrotas en 1 y no afecta a las victorias.
        Player player = new Player("Perdedor", 3, 8);

        player.incrementLosses(); //jugador pierde una partida

        assertEquals(9, player.getGamesLost(), "El contador de derrotas no se incrementó correctamente.");
        assertEquals(3, player.getGamesWon(), "El contador de victorias no debería cambiar al perder.");
    }

    @Test
    void test5_MultipleIncrements_ShouldUpdateStatsCorrectly() {
        //secuencia de victorias y derrotas actualiza el estado del objeto Player de forma consistente
        Player player = new Player("Jugador Activo", 10, 10);

        //secuencia de partidas
        player.incrementWins();
        player.incrementLosses();
        player.incrementWins();
        player.incrementWins();

        //el resultado final debería ser 10+3=13 victorias y 10+1=11 derrotas.
        assertEquals(13, player.getGamesWon(), "El total de victorias después de varias partidas es incorrecto.");
        assertEquals(11, player.getGamesLost(), "El total de derrotas después de varias partidas es incorrecto.");
    }
}