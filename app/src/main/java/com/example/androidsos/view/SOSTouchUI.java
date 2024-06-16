package com.example.androidsos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidsos.R;
import com.example.androidsos.controller.Controller;

public class SOSTouchUI extends AppCompatActivity {

    private Button btn_s, btn_o;
    private SOSGameBoard sosGameBoard;
    private Controller controller;
    private TextView txt_turn, p1_scoreTxt, p2_score_Txt, gameState;
    private final String[] currentSymbol = new String[]{"S", "O"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sostouch_ui);
        controller = new Controller();
        controller.setCurrentSymbol(currentSymbol[0]);
        controller.resetGame();

        sosGameBoard = findViewById(R.id.sosGameBoard);

        txt_turn = findViewById(R.id.txt_player_turn);
        //txt_turn.setTextColor(R.styleable.sosBoard_winningLineColor);
        txt_turn.setText("Player 1");

        p1_scoreTxt = findViewById(R.id.p1_score);
        //p1_scoreTxt.setTextColor(R.styleable.sosBoard_sColor);

        p2_score_Txt = findViewById(R.id.p2_score);
        //p2_score_Txt.setTextColor(R.styleable.sosBoard_oColor);

        gameState = findViewById(R.id.txt_state);
        gameState.setVisibility(View.GONE);
        //gameState.setTextColor(R.styleable.sosBoard_winningLineColor);



        controller.connectUI(sosGameBoard,
                (score, player) -> runOnUiThread(() -> updateScore(score, player)),
                (player) -> runOnUiThread(()-> updateTurn(player)),
                () -> runOnUiThread(this::endGameState)
        );
        sosGameBoard.setActivity(this);


        btn_s = findViewById(R.id.btn_s);
        btn_s.setOnClickListener(listener -> {
            controller.setCurrentSymbol(currentSymbol[0]);
        });

        btn_o = findViewById(R.id.btn_o);
        btn_o.setOnClickListener(listener -> {
            controller.setCurrentSymbol(currentSymbol[1]);
        });

        sosGameBoard = findViewById(R.id.sosGameBoard);

    }

    private void endGameState() {
        gameState.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        controller.resetGame();
    }
    public void updateScore(int score, int player){
        switch (player){
            case 1: {

                p1_scoreTxt.setText(String.valueOf(score));
                break;
            }
            case 2: {

                p2_score_Txt.setText(String.valueOf(score));
                break;
            }
        }
    }
    public void updateTurn(int turn){
        switch(turn){
            case 1: {
                //txt_turn.setTextColor(R.styleable.sosBoard_sColor);
                txt_turn.setText(R.string.player1_id);
                break;
            }
            case 2: {
                //txt_turn.setTextColor(R.styleable.sosBoard_oColor);
                txt_turn.setText(R.string.player2_id);
                break;
            }
        }
    }
    public void hideState(View view){
        view.setVisibility(View.GONE);
    }

}