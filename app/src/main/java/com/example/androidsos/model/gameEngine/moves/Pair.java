package com.example.androidsos.model.gameEngine.moves;

public class Pair<X, Y> {
	private X x;
	private Y y;
	public Pair(X x, Y y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	public X getX() {return this.x;}
	public Y getY() {return this.y;}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
