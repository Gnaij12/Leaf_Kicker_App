package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.LightingColorFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    DrawView mDrawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawView = findViewById(R.id.drawView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDrawView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawView.resume();
    }
}