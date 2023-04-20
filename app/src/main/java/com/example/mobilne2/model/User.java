package com.example.mobilne2.model;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String LOGGEDIN = "loggedin";
    private String username;
    private String password;
    private String email;
    private boolean loggedIn;

    public User(String username, String password, String email, boolean loggedIn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.loggedIn = loggedIn;
    }

    public static boolean checkPassword(String password) {
        int chars = 0;
        int digits = 0;
        int capitals = 0;

        for(char c : password.toCharArray()) {
            if(c >= '0' && c <= '9') {
                digits++;
            }
            else if(c >= 'A' && c <= 'Z') {
                chars++;
                capitals++;
            }
            else if(c>='a' && c <= 'z') {
                chars++;
            }
            else {
                return false;
            }
        }

        return chars >= 5 && capitals >= 1 && digits >= 1;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
