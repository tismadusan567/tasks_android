package com.example.mobilne2.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.viewpager.PagerAdapter;
import com.example.mobilne2.view.viewpager.TaskDetailsPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details_activity_layout);
        init();
    }

    private void init() {
        initViewPager();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.detailsViewPager);
        Task task = new Task("test", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "desc", Task.Priority.HIGH);
        Task task2 = new Task("test2", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "desc", Task.Priority.MID);
        Task task3 = new Task("test3", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "desc", Task.Priority.LOW);
        List<Task> l = new ArrayList<>();
        l.add(task);
        l.add(task2);
        l.add(task3);
        viewPager.setAdapter(new TaskDetailsPagerAdapter(getSupportFragmentManager(), l));
    }

}
