package com.reversedots.model;

public class Player implements java.io.Serializable {
    private String name;
    private int gamesWon;
    private int gamesLost;

    public Player(String name) {
        this(name, 0, 0);
    }

    //contructor para cargar jugadores con estadisticas previas
    public Player(String name, int gamesWon, int gamesLost) {
        this.name = name;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    public String getName() { return name; }
    public int getGamesWon() { return gamesWon; }
    public int getGamesLost() { return gamesLost; }

    public void incrementWins() { this.gamesWon++; }
    public void incrementLosses() { this.gamesLost++; }
}
