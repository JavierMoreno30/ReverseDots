package com.reversedots.repository;

import com.reversedots.model.GameData;
import java.io.*;

//Implementación de GameRepository que utiliza archivos para persistir los datos del juego.
public class FileGameRepository implements GameRepository {
    @Override
    public void save(GameData data, String filePath) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        }
    }
    @Override
    public GameData load(String filePath) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameData) ois.readObject();
        }
    }
}