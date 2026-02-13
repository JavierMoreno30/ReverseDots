package com.reversedots.repository;

import com.reversedots.model.GameData;

import java.io.*;
import java.nio.file.*;

public class FileGameRepository implements GameRepository {
    private static final String SAVES_DIR = "saves";

    @Override
    public void save(GameData data, String filePath) throws Exception {
        Path dir = Paths.get(SAVES_DIR);
        Files.createDirectories(dir);

        Path fullPath = dir.resolve(filePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath.toFile()))) {
            oos.writeObject(data);
        }
    }

    @Override
    public GameData load(String filePath) throws Exception {
        Path fullPath = Paths.get(SAVES_DIR).resolve(filePath);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullPath.toFile()))) {
            return (GameData) ois.readObject();
        }
    }
}
