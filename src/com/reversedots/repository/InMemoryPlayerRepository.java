package com.reversedots.repository;

import com.reversedots.model.Player;
import java.util.ArrayList;
import java.util.List;

public class InMemoryPlayerRepository implements PlayerRepository {
    private List<Player> players = new ArrayList<>();

    @Override
    public void save(Player player) {
        if (findByName(player.getName()) == null) {
            players.add(player);
        }
    }

    @Override
    public Player findByName(String name) {
        return players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Player> findAll() {
        return new ArrayList<>(players);
    }
}