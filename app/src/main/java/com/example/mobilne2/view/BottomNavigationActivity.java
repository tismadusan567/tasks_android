package com.example.mobilne2.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.mobilne2.R;
import com.example.mobilne2.view.viewpager.PagerAdapter;

public class BottomNavigationActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        init();
    }

    private void init() {
        initViewPager();
        initNavigation();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private void initNavigation() {
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
                case R.id.navigation_2: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false); break;
                case R.id.navigation_3: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            return true;
        });
    }

}