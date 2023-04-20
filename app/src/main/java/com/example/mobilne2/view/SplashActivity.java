package com.example.mobilne2.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilne2.MainActivity;
import com.example.mobilne2.R;
import com.example.mobilne2.model.User;

import java.io.IOException;
import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("dnevnjak", Context.MODE_PRIVATE);

            String username = sharedPreferences.getString(User.USERNAME,  null);
            String password = sharedPreferences.getString(User.PASSWORD, null);
            String email = sharedPreferences.getString(User.EMAIL, null);
            boolean isLoggedIn = sharedPreferences.getBoolean(User.LOGGEDIN, false);

            if (!isLoggedIn || username == null) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            User user = new User(
                    username,
                    password,
                    email,
                    true
            );

            Intent intent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        }, 2000);
    }
}
