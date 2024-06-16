package com.example.androidsos.controller;

import com.example.androidsos.model.gameEngine.GameEngine;
import com.example.androidsos.model.gameEngine.IEndGameState;
import com.example.androidsos.model.gameEngine.IScoreUpdate;
import com.example.androidsos.model.gameEngine.IPlayerUpdate;
import com.example.androidsos.model.gameEngine.moves.Move;
import com.example.androidsos.model.gameEngine.Player;
import com.example.androidsos.model.grid.Grid;
import com.example.androidsos.model.gameEngine.moves.MoveDAO;
import com.example.androidsos.model.property.Property;
import com.example.androidsos.model.property.PropertyListener;
import com.example.androidsos.view.IDisplayer;
import com.example.androidsos.view.SOSGameBoard;
import java.util.List;

public class Controller {
    private String symbol=null;
    private SOSGameBoard sosGameBoard;
    private final GameEngine gameEngine;
    private Player player1, player2;
    private final Runnable updateUIRunnable;
    private List<Move> moves;
    private int turn = 1;
    private IScoreUpdate<Integer, Integer> scoreUpdate;
    private IPlayerUpdate iPlayerUpdate;
   private PropertyListener<Boolean> gameStateListener;
   private IEndGameState iEndGameState;

    public Controller(){
        player1 = new Player(123);
        player2 = new Player(567);
        gameEngine = new GameEngine(5, player1, player2, this);
        updateUIRunnable = ()-> {
            sosGameBoard.updateUI();
        };
        setCurrentSymbol("S");
        gameStateListener = (property, oldValue, newValue) -> {
            if(newValue){
                iEndGameState.endGameState();
            }
        };

    }

    public void connectUI(SOSGameBoard sosGameBoard,
                          IScoreUpdate<Integer,
                          Integer> scoreUpdate,
                          IPlayerUpdate iPlayerUpdate,
                          IEndGameState iEndGameState) {
        this.sosGameBoard = sosGameBoard;
        this.scoreUpdate = scoreUpdate;
        this.iPlayerUpdate = iPlayerUpdate;
        this.iEndGameState = iEndGameState;
        if(sosGameBoard != null){
            sosGameBoard.connectController(this, updateUIRunnable);
        }
    }

    public void setCurrentSymbol(String s) {
        this.symbol = s;
    }

    public void placePiece(int row, int col) {
        // 1. Place piece
        boolean isPlaced = gameEngine.placePiece(symbol, row, col);

        boolean isCombination;
        if(isPlaced){
            // 2. check for combinations
            gameEngine.makeMove();
            isCombination = gameEngine.checkSequence(symbol, row, col);
            if(isCombination){
                moves = gameEngine.getGameMoves();
                for(Move move : moves){
                    sosGameBoard.getWinningLineMove(new MoveDAO(move, turn));
                    scoreUpdate.updateScore(gameEngine.getScore(turn), turn);
                }
            }
            else{
                turn = gameEngine.togglePlayerTurn();
                updatePlayerTurn(turn);
            }
        }
    }


    public Grid getGameBoard() {
        return gameEngine.getGameBoard();
    }

    public void resetGame() {
        gameEngine.reset();
    }

    public void updatePlayerTurn(int turn) {
        iPlayerUpdate.updatePlayer(turn);
    }
}
