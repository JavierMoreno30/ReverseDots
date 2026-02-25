package com.reversedots.repository;

import com.reversedots.model.GameData;

import java.io.*;
import java.nio.file.*;
//utiliza serializacion para poder guardar y cargar el estado completo del juego en un archivo binario(sin preocuparse por formatos)
public class FileGameRepository implements GameRepository {
    private static final String SAVES_DIR = "saves";

//save crea el directorio "saves" en caso de que no exista y luego guarda el objeto GameData
    @Override
    public void save(GameData data, String filePath) throws Exception {
        Path dir = Paths.get(SAVES_DIR);
        Files.createDirectories(dir);
        Path fullPath = dir.resolve(filePath);
//utiliza ObjectOutputStream para escribir el objeto GameData en un archivo binario
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath.toFile()))) {
            oos.writeObject(data);
        }
    }
//load lee el archivo y devuelve el objeto GameData modificado, si no es valido o no existe, se lanza una excepción
    @Override
    public GameData load(String filePath) throws Exception {
        Path fullPath = Paths.get(SAVES_DIR).resolve(filePath);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullPath.toFile()))) {
            return (GameData) ois.readObject();
        }
    }
}
