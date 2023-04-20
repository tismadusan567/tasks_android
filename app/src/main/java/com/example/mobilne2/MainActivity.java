package com.example.mobilne2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilne2.model.User;
import com.example.mobilne2.view.BottomNavigationActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView emailET;
    private TextView usernameET;
    private TextView passwordET;
    private Button loginBtn;
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
        loginBtn = findViewById(R.id.loginBtn);
        emailET = findViewById(R.id.emailEditText);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
    }

    private void initListeners() {
        loginBtn.setOnClickListener((v -> {
            boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches();
            if (!isValidEmail) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (usernameET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream inputStream = getResources().openRawResource(R.raw.password);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String savedPassword;
            try {
                savedPassword = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!savedPassword.equals(passwordET.getText().toString())) {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("dnevnjak", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            User user = new User(
                    usernameET.getText().toString(),
                    passwordET.getText().toString(),
                    emailET.getText().toString(),
                    true
            );

            String encodedUser = null;
            try {
                encodedUser = Base64.encodeToString(user.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            editor.putString(User.KEY, encodedUser);

            editor.apply();


            Intent intent = new Intent(this, BottomNavigationActivity.class);
            startActivity(intent);
        }));
    }


}