package com.reversedots.model;

public class Player implements java.io.Serializable {
    private String name;
    private int gamesWon;
    private int gamesLost;

    public Player(String name) {
        this.name = name;
        this.gamesWon = 0;
        this.gamesLost = 0;
    }

    //Getters and Setters
    public String getName() { return name;
    }
    public int getGamesWon() { return gamesWon;
    }
    public void incrementWins() { this.gamesWon++;
    }
    public int getGamesLost() { return gamesLost;
    }
    public void incrementLosses() { this.gamesLost++;
    }
}
