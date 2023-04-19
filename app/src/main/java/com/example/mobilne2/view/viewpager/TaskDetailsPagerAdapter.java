package com.example.mobilne2.view.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.fragments.TaskDetailsFragment;

import java.util.List;

public class TaskDetailsPagerAdapter extends FragmentPagerAdapter {
    private List<Task> tasks;

    public TaskDetailsPagerAdapter(@NonNull FragmentManager fm, List<Task> tasks) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new TaskDetailsFragment(tasks.get(position));
    }

    @Override
    public int getCount() {
        return tasks.size();
    }
}
