package com.example.androidsos.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androidsos.R;
import com.example.androidsos.controller.Controller;
import com.example.androidsos.model.gameEngine.IScoreUpdate;
import com.example.androidsos.model.gameEngine.moves.MoveDAO;
import com.example.androidsos.model.gameEngine.moves.Pair;

import java.util.HashSet;
import java.util.List;

public class SOSGameBoard extends View {
    private final int GRID_SIZE = 5;
    private int boardColor, oColor, sColor, winningLineColor, winningLineColor2;
    private final Paint paint = new Paint();
    private int cellSize;
    private Controller controller;
    private Runnable updateUIRunnable;
    private Activity sosGameActivity;
    private HashSet<MoveDAO> winningLines;
    private double OFFSET;
    private IScoreUpdate<Integer, Integer> scoreUpdate;

    public SOSGameBoard(Context context) {
        super(context);
    }

    public SOSGameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        winningLines = new HashSet<>();
        TypedArray res = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.sosBoard,
                0,
                0

        );

        try{
            boardColor = res.getInt(R.styleable.sosBoard_boardColor, Color.BLACK);
            sColor = res.getInt(R.styleable.sosBoard_sColor, 0);
            oColor = res.getInt(R.styleable.sosBoard_oColor, 0);
            winningLineColor = res.getInt(R.styleable.sosBoard_winningLineColor, 0);
            winningLineColor2 = res.getInt(R.styleable.sosBoard_winningLineColor2, 0);
        }finally {
            res.recycle();
        }
    }

    public SOSGameBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SOSGameBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int height, int width){
        super.onMeasure(width, height);
        int dimension = Math.min(getMeasuredHeight(), getMeasuredWidth());
        cellSize = dimension/GRID_SIZE;
        OFFSET = cellSize*0.3;
        setMeasuredDimension(dimension, dimension);
    }
    @Override
    protected void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawRect();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        //paint.setColor("#FF6D00");
        drawGameBoard(canvas);
        drawMarkers(canvas);
        drawWinningLines(canvas);
    }

    private void drawWinningLines(Canvas canvas) {
        if(winningLines.size() < 1){
            return;
        }
        //paint.setColor(winningLineColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            winningLines.forEach(dao ->{
                int configuration = dao.getConfiguration();
                drawWinningLine(canvas, dao, configuration);
            });
        }
    }

    private void drawWinningLine(Canvas canvas, MoveDAO moveDAO, int configuration) {
        int lineColor = moveDAO.getTurn();
        paint.setColor(lineColor == 1 ? winningLineColor: winningLineColor2);
        switch(configuration){
            case 0:
            case 4: {
                drawVerticalWinningLine(canvas, moveDAO);
                break;
            }
            case 1:
            case 5:{
                drawOffDiagonalWinningLine(canvas, moveDAO);
                break;
            }
            case 2:
            case 6: {
                drawHorizontalWinningLine(canvas, moveDAO);
                break;
            }
            case 3:
            case 7:{
                drawDiagonalWinningLine(canvas, moveDAO);
            }
        }
    }

    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardColor);
        paint.setStrokeWidth(5);

        for(int col = 0; col <= GRID_SIZE; col++){
            canvas.drawLine(col*cellSize, 0, col*cellSize, canvas.getWidth(), paint);
        }
        for(int row = 0; row <= GRID_SIZE; row++){
            canvas.drawLine(0, row*cellSize, canvas.getWidth(), row*cellSize, paint);
        }
    }
    private void drawHorizontalWinningLine(Canvas canvas, MoveDAO moveDAO){

        canvas.drawLine((float) (moveDAO.getYStart()*cellSize+OFFSET), (float)(moveDAO.getXStart()*cellSize+cellSize/2),
                (float) (moveDAO.getYEnd()*cellSize + cellSize - OFFSET), (float)(moveDAO.getXEnd()*cellSize + cellSize/2), paint);
    }
    private void drawVerticalWinningLine(Canvas canvas, MoveDAO moveDAO){
        canvas.drawLine((float)(moveDAO.getYStart()*cellSize + cellSize/2), (float)(moveDAO.getXStart()*cellSize+cellSize-OFFSET),
                (float)(moveDAO.getYEnd()*cellSize + cellSize/2), (float)(moveDAO.getXEnd()*cellSize+OFFSET), paint);
    }
    private void drawDiagonalWinningLine(Canvas canvas, MoveDAO moveDAO){
        canvas.drawLine((float)(moveDAO.getYStart()*cellSize+OFFSET), (float)(moveDAO.getXStart()*cellSize+OFFSET),
                (float)(moveDAO.getYEnd()*cellSize + cellSize-OFFSET), (float)(moveDAO.getXEnd()*cellSize + cellSize-OFFSET), paint);
    }
    private void drawOffDiagonalWinningLine(Canvas canvas, MoveDAO moveDAO){
        canvas.drawLine((float)(moveDAO.getYStart()*cellSize+OFFSET), (float)(moveDAO.getXStart()*cellSize + cellSize-OFFSET),
                (float)(moveDAO.getYEnd()*cellSize + cellSize-OFFSET), (float)(moveDAO.getXEnd()*cellSize+OFFSET), paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            int row = (int)Math.ceil(motionEvent.getY() / cellSize);
            int col = (int)Math.ceil(motionEvent.getX() / cellSize);
            int newRow = row-1;
            int newCol = col-1;
            controller.placePiece(newRow, newCol);
            invalidate();
            return true;
        }
        return false;
    }
    public void drawMarkers(Canvas canvas){
        if(canvas != null){
            for(int row = 0; row < GRID_SIZE; row++){
                for(int col = 0; col < GRID_SIZE; col++){
                    String marker = controller.getGameBoard().getPiece(row, col);
                    paint.setColor(marker.equalsIgnoreCase("s")? sColor: oColor);
                    paint.setTextSize(40);
                    paint.setStrokeWidth(5);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(marker,
                            (float)(col*cellSize + cellSize/2), (float)(row*cellSize+cellSize*0.60),
                            paint);
                }
            }
        }
    }

    public void connectController(Controller controller, Runnable updateUIRunnable) {
        this.controller = controller;
        this.updateUIRunnable = updateUIRunnable;
    }

    public void updateUI(List<Pair<Integer, Integer>> playerMovesList) {
    }
    public void updateUI(){
        sosGameActivity.runOnUiThread(()->{
            updateUIRunnable.run();
        });
    }

    public void setActivity(Activity sosTouchUI) {
        this.sosGameActivity = sosTouchUI;
    }

    public void getWinningLineMove(MoveDAO move) {
        winningLines.add(move);
    }

}
