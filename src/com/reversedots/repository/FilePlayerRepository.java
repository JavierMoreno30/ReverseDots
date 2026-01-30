package com.reversedots.repository;

import com.reversedots.model.Player;
import java.io.*;
import java.util.*;

public class FilePlayerRepository implements PlayerRepository {
    private final String FILE_PATH = "players.txt";
    //Guarda los jugadores en un archivo de texto simple
    @Override
    public void save(Player player) {
        Map<String, Player> allPlayers = loadAllAsMap();
        allPlayers.put(player.getName(), player);
        saveAll(allPlayers.values());
    }
    //Busca un jugador por nombre en el archivo
    @Override
    public Player findByName(String name) {
        return loadAllAsMap().get(name);
    }
    //Carga todos los jugadores desde el archivo
    @Override
    public List<Player> findAll() {
        return new ArrayList<>(loadAllAsMap().values());
    }
    //Carga todos los jugadores en un mapa para acceso rápido
    private Map<String, Player> loadAllAsMap() {
        Map<String, Player> players = new HashMap<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return players;
    //Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Formato: nombre,ganadas,perdidas
                Player p = new Player(data[0]);
                p.incrementWins(); // Ajustar según los valores guardados
                // Aquí deberías tener un método para setear valores exactos
                players.put(p.getName(), p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
//Guarda todos los jugadores en el archivo
    private void saveAll(Collection<Player> players) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Player p : players) {
                pw.println(p.getName() + "," + p.getGamesWon() + "," + p.getGamesLost());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}