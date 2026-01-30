package com.reversedots.repository;

import com.reversedots.model.Player;

import java.util.List;

public interface PlayerRepository {
    void save(Player player);
    Player findByName(String name);
    List<Player> findAll();
}
