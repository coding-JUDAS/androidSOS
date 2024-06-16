package com.example.androidsos.model.grid;

import androidx.annotation.NonNull;

public class Grid {
	/**
	 * Grid is a Singleton class that stores the Data structure that is used represent the
	 * game model and algorithms to access and manipulate the game data.
	 */

	// Value used to determine the dimensions of the grid upon initialization.
	public static int grid_size;

	//region NOTES ON SINGLETON NATURE OF CLASS
	// Class contains a reference to itself for the purpose of implementing the Singleton Pattern.
	// For this particular scenario, we are only using the SINGLETON pattern for EDUCATIONAL purposes
	// only, as the latter would not scale for multiple players of more than two at a time, or server
	// hosting for multiple tournaments etc, in that case, the SINGLETON pattern would probably
	// not be the best idea.
	//endregion
	private static Grid GRID = null;
	// internal board represented by 2D-Array
	private static String[][]board = null;
	//supplementary data structure to increase performance, avoiding heavy computation
	private static boolean[][] isOccupied;
	// constructor MUST be private in order to lock the class from from having more than one
	// instance at a TIME.
	private Grid() {}
	public static Grid getInstance(int size) {
		grid_size = size;
		if(GRID == null){
			GRID = new Grid();
			initialize();
		}
		return GRID;
	}

	/**
	 * Method initializes the Grid[][] i.e 2D data structure to represent the game board, initially
	 * containing the EMPTY String.
	 */
	private static void initialize() {
		board = new String[grid_size][grid_size];
		isOccupied = new boolean[grid_size][grid_size];
		for(int row = 0; row < grid_size; row++) {
			for(int col = 0; col < grid_size; col++) {
				board[row][col] = "";
				isOccupied[row][col] = false;
			}
		}
	}

	/**
	 * Method is used to place a piece on the board. A piece is of type String and is either
	 * an 's' or an 'o', nothing else is placed on the board.
	 * @param piece that is to be placed on the board.
	 * @param col location on the grid to place the piece.
	 * @param row location on grid.
	 * @return
	 */
	public boolean placePiece(String piece, int row, int col){
		if(isOccupied[row][col]) {
			return false;
		}
		board[row][col] = piece;
		isOccupied[row][col] = true;
		return true;
		
	}
	public String removePiece(int row, int col) {
		String removedPiece = board[row][col];
		board[row][col] = "";
		isOccupied[row][col] = false;
		return removedPiece;
	}

	public String getPiece(int row, int col) {
		return board[row][col];
	}
	@NonNull
	@Override
	public String toString() {
		StringBuilder sr = new StringBuilder();
		sr.append("");
		for(int i = 0; i < grid_size; i++) {
			for(int j = 0; j < grid_size; j++) {
				sr.append("|").append(board[i][j]);
				for(int k = 0; k < grid_size; k++) {
					sr.append("_");
				}
			}
			sr.append("|").append("\n");
		}
		return sr.toString();
		
	}

    public void reset() {
		initialize();
    }
}
