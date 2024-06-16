package com.example.androidsos.model.gameEngine.moves;

import java.util.List;

public class PlayerMove extends Move{

    public PlayerMove(List<Pair<Integer, Integer>> movesList, String pattern, int configuration) {
        super(movesList, pattern, configuration);
    }
}
