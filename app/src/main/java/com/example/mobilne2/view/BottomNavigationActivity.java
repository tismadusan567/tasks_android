package com.example.mobilne2.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.viewpager.widget.ViewPager;

import com.example.mobilne2.model.User;
import com.example.mobilne2.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.mobilne2.R;
import com.example.mobilne2.view.viewpager.PagerAdapter;

import java.util.Calendar;

public class BottomNavigationActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RecyclerViewModel recyclerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewModel.loadFromDatabase();
    }

    private void init() {
        initViewPager();
        initNavigation();

        recyclerViewModel = new ViewModelProvider(this).get(RecyclerViewModel.class);

        recyclerViewModel.getPredicates().observe(this, predicates -> {
            recyclerViewModel.filterTasksByPredicates();
        });

        User user = (User) getIntent().getExtras().get("user");
        recyclerViewModel.getUser().setValue(user);

    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private void initNavigation() {
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
                case R.id.navigation_2:
                    recyclerViewModel.getCurrentDay().setValue(Calendar.getInstance().getTime());
                    viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false);
                    break;
                case R.id.navigation_3: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            return true;
        });
    }

}