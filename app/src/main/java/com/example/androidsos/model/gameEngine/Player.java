package com.example.androidsos.model.gameEngine;


import com.example.androidsos.model.property.Property;

public class Player {
	private final int playerId;
	private String playerName;
	private int points = 0;
	public Property<Integer> scoreProperty = new Property<>(this, 0);

	public Player(int playerId) {
		this.playerId = playerId;
	}

	public int getPlayerID() {return this.playerId;}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getPoints() {
		return this.points;
	}
	
}
