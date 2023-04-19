package com.example.mobilne2.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Database;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.viewpager.PagerAdapter;
import com.example.mobilne2.view.viewpager.TaskDetailsPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details_activity_layout);
        init();
    }

    private void init() {
        task = (Task) getIntent().getExtras().get("task");
        initViewPager();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.detailsViewPager);

        List<Task> tasks = new ArrayList<>(Database.getInstance().getAllTasks());
        int index = -1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                index = i;
                break;
            }
        }
        Log.d("lool", index + " " + task.getTitle());

        viewPager.setAdapter(new TaskDetailsPagerAdapter(getSupportFragmentManager(), tasks));
        viewPager.setCurrentItem(index);
    }

}
