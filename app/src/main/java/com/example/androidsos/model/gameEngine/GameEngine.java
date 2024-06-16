package com.example.androidsos.model.gameEngine;

import com.example.androidsos.controller.Controller;
import com.example.androidsos.model.gameEngine.moves.Move;
import com.example.androidsos.model.gameEngine.moves.Pair;
import com.example.androidsos.model.gameEngine.moves.PlayerMove;
import com.example.androidsos.model.grid.Grid;
import com.example.androidsos.model.property.Property;

import java.util.ArrayList;
import java.util.List;


public class GameEngine{

	private final int[] xDir = {0, 1, 1, 1, 0, -1, -1, -1};
	private final int[] yDir = {-1, -1, 0, 1, 1, 1, 0, -1};
	private final Player player_1;
	private final Player player_2;
	private int[] gameScore = new int[]{0, 0};
	private final int size;
	private final Grid gameBoard;
	private int turn = 1;
	private final Property<Player> curPlayerProperty = new Property<>(this, null);
	private int availableMoves;
	private List<Pair<Integer, Integer>> playerMovesList = null;
	private final List<Move> gameMoves;
	private Move move = null;
	private int numberOfMoves = 0;
	private Controller controller = null;
	private final IGameScoreRenderer iGameScoreRenderer;
	public Property<Boolean> gameOver = new Property<>(this, false);
	
	public GameEngine(int size, Player player_1, Player player_2, Controller controller) {
		this.size = size;
		this.controller = controller;
		this.player_1 = player_1;
		this.player_2 = player_2;

		gameMoves = new ArrayList<>();
		availableMoves = size*size;
		gameBoard = Grid.getInstance(size);

		iGameScoreRenderer = player -> {
			int score = gameScore[player-1];
			score = score + 1;
			gameScore[player-1] = score;
		};
	}


	public void endGameState() {
		// TODO Auto-generated method stub
		if(player_1.scoreProperty.get() > player_2.scoreProperty.get()) {
			System.out.println("\t\t\tplayer 1 WON the game");
		}
		else if(player_1.scoreProperty.get() < player_2.scoreProperty.get()) {
			System.out.println("\t\t\tplayer 1 WON the game");
		}
		else {
			System.out.println("\t\t\t ...GAME is a DRAW ! ! ! ");
		}
	}

	public boolean checkSequence(String pieceString, int row, int col) {
		// TODO Auto-generated method stub
		// two cases - an 'S' or 'O' is placed
		// case one - 'S'
		boolean[][] isVisited = new boolean[size][size];
		if(pieceString.equalsIgnoreCase("s"))
			return checkSsequence(pieceString, row, col,isVisited);
		return checkOsequence(pieceString, row, col, isVisited);
		
	}

	private boolean checkOsequence(String pieceString, int row, int col, boolean[][] isVisited) {
		// TODO Auto-generated method stub
		boolean isCombination = false;
		isVisited[row][col] = true;
		for(int i = 0; i < xDir.length; i++) {
			playerMovesList = new ArrayList<>();
			playerMovesList.add(new Pair<>(row, col));
			// obtain next col-row cell
			int nextRow = yDir[i] + row;
			int nextCol = xDir[i] + col;
			
			if(isValid(nextRow, nextCol) && !isVisited[nextRow][nextCol]) {
				isVisited[nextRow][nextCol] = true;
				String pieceString1 = gameBoard.getPiece(nextRow, nextCol);
				
				if(pieceString1.equalsIgnoreCase("s")) {
					playerMovesList.add(new Pair<>(nextRow, nextCol));
					// obtain opposite col-row cell
					int k = i;
					int cycle = 0;
					while(cycle < 4) {
						k++;
						if(k >= yDir.length)
							k=0;
						cycle++;
					}
					int oppositeRow = yDir[k] + row;
					int oppositeCol = xDir[k] + col;
					
					if(isValid(oppositeRow, oppositeCol) && !isVisited[oppositeRow][oppositeCol]) {
						isVisited[oppositeRow][oppositeCol] = true;
					
						if(gameBoard.getPiece(oppositeRow, oppositeCol).equalsIgnoreCase("s")) {
							playerMovesList.add(new Pair<>(oppositeRow, oppositeCol));
							move = new PlayerMove(playerMovesList, pieceString, i);
							gameMoves.add(move);
							isCombination = true;
							updateScore();
						}
					}
				}
			}
		}
		return isCombination;
	}

	public boolean checkSsequence(String pieceString, int row, int col, boolean[][] isVisited) {
		// TODO Auto-generated method stub
		if(numberOfMoves < 3){
			return false;
		}
		boolean cycle = false;

		isVisited[row][col] = true;
		for(int i = 0; i < xDir.length; i++) {
			playerMovesList = new ArrayList<>();
			playerMovesList.add(new Pair<>(row, col));

			int nextRow = yDir[i] + row;
			int nextCol = xDir[i] + col;
			if(isValid(nextRow, nextCol) && !isVisited[nextRow][nextCol]) {
				//1. mark as visited for optimisation purposes.

				isVisited[nextRow][nextCol] = true;
				String curString = gameBoard.getPiece(nextRow, nextCol);
				
				if(curString.equalsIgnoreCase("o")) {
					playerMovesList.add(new Pair<>(nextRow, nextCol));
					nextRow += yDir[i];
					nextCol += xDir[i];
					
					if(isValid(nextRow, nextCol) && !isVisited[nextRow][nextCol]) {
						// mark as visited.
						isVisited[nextRow][nextCol] = true;
						if(gameBoard.getPiece(nextRow, nextCol).equalsIgnoreCase("s")) {
							if(i > 3){
								playerMovesList.add(0, new Pair<>(nextRow, nextCol));
							}
							else{
								playerMovesList.add(1, new Pair<>(nextRow, nextCol));
							}

							move = new PlayerMove(playerMovesList, pieceString, i);
							gameMoves.add(move);
							cycle = true;
							updateScore();
						}

					}
				}
			}

		}
		return cycle;
	}

	private boolean isValid(int row, int col) {
		// TODO Auto-generated method stub
		return (row > -1 && row < size) && (col > -1 && col < size);
	}

	public boolean placePiece(String pieceString, int row, int col) {
		// TODO Auto-generated method stub
		if(numberOfMoves < availableMoves)
			return gameBoard.placePiece(pieceString, row, col);
		gameOver.set(true);
		return false;
	}

	public int togglePlayerTurn() {
		// TODO Auto-generated method stub
		if(this.turn == 2) {
			this.turn = 1;
		}
		else {
			this.turn = 2;
		}
		//controller.updatePlayerTurn(turn);
		return this.turn;
	}

	public Grid getGameBoard() {
		return this.gameBoard;
	}

	public List<Move> getGameMoves() {
		return gameMoves;
	}

	public void reset() {
		gameBoard.reset();
		numberOfMoves=0;
		gameOver.set(false);
	}
	public int getScore(int player){
		if(player > 0 && player <= gameScore.length){
			return gameScore[player-1];
		}
		return 0;
	}

	public int getTurn() {
		return this.turn;
	}
	public void updateScore(){
		iGameScoreRenderer.renderScore(getTurn());
	}
	public int[] getGameScore(){
		return this.gameScore;
	}

	public void makeMove() {
		numberOfMoves++;
	}
}
