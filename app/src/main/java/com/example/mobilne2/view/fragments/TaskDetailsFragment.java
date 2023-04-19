package com.example.mobilne2.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskDetailsFragment extends Fragment {

    private TextView dateTextView;
    private TextView timeTextView;
    private TextView titleTextView;
    private TextView descTextView;
    private FloatingActionButton editBtn;
    private FloatingActionButton deleteBtn;

    private final Task task;

    public TaskDetailsFragment(Task task) {
        super(R.layout.task_details_fragment_layout);

        this.task = task;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        initView(view);
        fillView();
        initListeners();
    }

    private void initView(View view) {
        dateTextView = view.findViewById(R.id.detailsDateTextView);
        timeTextView = view.findViewById(R.id.detailsTimeTextView);
        titleTextView = view.findViewById(R.id.detailsTitleTextView);
        descTextView = view.findViewById(R.id.detailsDescTextView);
        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
    }

    private void fillView() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd. yyyy.", Locale.US);
        dateTextView.setText(sdf.format(task.getStartTime()));

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.US);
        String text = sdf2.format(task.getStartTime()) + " - " + sdf2.format(task.getEndTime());
        timeTextView.setText(text);

        titleTextView.setText(task.getTitle());
        descTextView.setText(task.getDescription());
    }

    private void initListeners() {
        editBtn.setOnClickListener(v -> {
            Log.d("lool", task.getTitle());
        });
    }
}

