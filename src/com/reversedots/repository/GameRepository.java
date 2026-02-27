package com.reversedots.repository;

import com.reversedots.model.GameData;

//Interfaz para la persistencia de datos
public interface GameRepository {
    void save(GameData data, String filePath) throws Exception;
    GameData load(String filePath) throws Exception;
}