package com.reversedots.repository;

import com.reversedots.model.GameData;

/**
 * Interfaz para definir las operaciones de persistencia de una partida completa.
 */
public interface GameRepository {
    void save(GameData data, String filePath) throws Exception;
    GameData load(String filePath) throws Exception;
}