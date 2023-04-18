package com.example.mobilne2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mobilne2.view.BottomNavigationActivity;

public class MainActivity extends AppCompatActivity {

    private Button openBottomNavigationBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        initListeners();
    }

    private void initView() {
        openBottomNavigationBtn = findViewById(R.id.openBottomNavigationBtn);
    }

    private void initListeners() {
        openBottomNavigationBtn.setOnClickListener((v -> {
            Intent intent = new Intent(this, BottomNavigationActivity.class);
            startActivity(intent);
        }));
    }


}