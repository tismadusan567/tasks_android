package com.example.mobilne2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilne2.model.User;
import com.example.mobilne2.view.BottomNavigationActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

            String savedPassword = "";

            try {
                InputStream inputStream = openFileInput("password.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    savedPassword = bufferedReader.readLine();

                    inputStream.close();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }

            if (!savedPassword.equals(passwordET.getText().toString())) {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("dnevnjak", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(User.USERNAME,  usernameET.getText().toString());
            editor.putString(User.PASSWORD, passwordET.getText().toString());
            editor.putString(User.EMAIL, emailET.getText().toString());
            editor.putBoolean(User.LOGGEDIN, true);

            editor.apply();

            User user = new User(
                    usernameET.getText().toString(),
                    passwordET.getText().toString(),
                    emailET.getText().toString(),
                    true
            );

            Intent intent = new Intent(this, BottomNavigationActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }));
    }


}