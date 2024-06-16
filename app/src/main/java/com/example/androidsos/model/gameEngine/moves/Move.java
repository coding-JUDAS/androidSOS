package com.example.androidsos.model.gameEngine.moves;

import java.io.Serializable;
import java.util.List;

public abstract class Move implements Serializable {
	private final List<Pair<Integer, Integer>> movesList;
	private final String pattern;
	private final int configuration;

	public Move(final List<Pair<Integer, Integer>> movesList, final String pattern, int configuration) {
		this.movesList = movesList;
		this.pattern = pattern;
		this.configuration = configuration;
	}

	public List<Pair<Integer, Integer>> getMovesList() {
		return movesList;
	}

	public String getPattern() {
		return pattern;
	}

	public int getConfiguration() {
		return configuration;
	}
}
