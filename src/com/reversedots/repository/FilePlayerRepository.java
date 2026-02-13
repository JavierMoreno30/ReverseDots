package com.reversedots.repository;

import com.reversedots.model.Player;
import java.io.*;
import java.util.*;

public class FilePlayerRepository implements PlayerRepository {
    private final String FILE_PATH = "players.txt";

    @Override
    public void save(Player player) {
        Map<String, Player> allPlayers = loadAllAsMap();
        allPlayers.put(player.getName(), player);
        saveAll(allPlayers.values());
    }

    @Override
    public Player findByName(String name) {
        return loadAllAsMap().get(name);
    }

    @Override
    public List<Player> findAll() {
        return new ArrayList<>(loadAllAsMap().values());
    }

    private Map<String, Player> loadAllAsMap() {
        Map<String, Player> players = new HashMap<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return players;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Player p = new Player(data[0]);
                    // Aquí podrías añadir un método setStats en Player para cargar los datos reales
                    players.put(p.getName(), p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

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