package com.reversedots.repository;

import com.reversedots.model.Player;

import java.io.*;
import java.nio.file.*;
import java.util.*;
//guarda los datos en un archivo de texto con formato CSV
public class FilePlayerRepository implements PlayerRepository {

    private static final String SAVES_DIR = "saves";
    private static final String FILE_NAME = "players.txt";
    private final Path filePath;

    public FilePlayerRepository() {
        try {
            Path dir = Paths.get(SAVES_DIR);
            Files.createDirectories(dir); //crea carpeta saves si no existe

            this.filePath = dir.resolve(FILE_NAME);

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar players.txt", e);
        }
    }

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

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    int won = Integer.parseInt(data[1].trim());
                    int lost = Integer.parseInt(data[2].trim());

                    Player p = new Player(name, won, lost);
                    players.put(name, p);
                }
            }
        } catch (Exception ignored) {}

        return players;
    }

    private void saveAll(Collection<Player> players) {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath))) {
            for (Player p : players) {
                pw.println(p.getName() + "," + p.getGamesWon() + "," + p.getGamesLost());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
