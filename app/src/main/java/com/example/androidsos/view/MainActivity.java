package com.example.androidsos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.androidsos.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTouchUI = findViewById(R.id.btn_touch_ui);
        Button btnDragUI = findViewById(R.id.btn_drag_ui);

        btnTouchUI.setOnClickListener( listener ->{
            Intent intent = new Intent(MainActivity.this, SOSTouchUI.class);
            startActivity(intent);
        });

        btnDragUI.setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, SOSDragUI.class);
            startActivity(intent);
        });
    }
}