package com.example.androidsos.model.gameEngine.moves;

import java.io.Serializable;
import java.util.List;

public class MoveDAO implements Serializable {
    private final int turn;
    private final String pattern;
    private final int xStart;
    private final int yStart;
    private final int xEnd;
    private final int yEnd;
    private final int configuration;
    private List<Pair<Integer, Integer>> coordinates;

    public MoveDAO(Move move, int turn){
        this.turn = turn;
        this.coordinates = move.getMovesList();
        this.configuration = move.getConfiguration();
        pattern = move.getPattern();
        if(pattern.equalsIgnoreCase("s")){
            xStart = coordinates.get(0).getX();
            yStart = coordinates.get(0).getY();

        }
        else{
            xStart = coordinates.get(2).getX();
            yStart = coordinates.get(2).getY();

        }
        xEnd = coordinates.get(1).getX();
        yEnd = coordinates.get(1).getY();

    }

    public String getPattern() {
        return pattern;
    }

    public int getXStart() {
        return xStart;
    }

    public int getYStart() {
        return yStart;
    }

    public int getXEnd() {
        return xEnd;
    }

    public int getYEnd() {
        return yEnd;
    }

    public int getConfiguration() {
        return configuration;
    }

    public int getTurn() {
        return turn;
    }
}
