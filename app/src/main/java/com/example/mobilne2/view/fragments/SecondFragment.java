package com.example.mobilne2.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.recycler.task.TaskAdapter;
import com.example.mobilne2.view.recycler.task.TaskDiffItemCallback;
import com.example.mobilne2.viewmodel.RecyclerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SecondFragment extends Fragment {

    private SearchView searchView;
    private TextView dateTextView;
    private CheckBox checkBox;
    private Button lowBtn;
    private Button midBtn;
    private Button highBtn;
    private RecyclerView recyclerView;
    private FloatingActionButton addObligationBtn;

    private RecyclerViewModel recyclerViewModel;
    private TaskAdapter taskAdapter;


    public SecondFragment() {
        super(R.layout.fragment_second);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initObservers();
        initRecycler();
        initListeners();
    }

    private void initView(View view) {
        dateTextView = view.findViewById(R.id.dateTextView);
        checkBox = view.findViewById(R.id.checkBox);
        searchView = view.findViewById(R.id.searchView);
        lowBtn = view.findViewById(R.id.lowBtn);
        midBtn = view.findViewById(R.id.midBtn);
        highBtn = view.findViewById(R.id.highBtn);
        recyclerView = view.findViewById(R.id.listRv);
        addObligationBtn = view.findViewById(R.id.floatingAddObligationBtn);
    }

    private void initListeners() {
        addObligationBtn.setOnClickListener(v -> {
            Task newTask = new Task(
                    "novi",
                    new Date(Calendar.getInstance().getTime().getTime() + 1000*3600*20) ,
                    new Date(Calendar.getInstance().getTime().getTime() + 1000*3600*21),
                    "description",
                    Task.Priority.LOW
                    );
            recyclerViewModel.addTask(newTask);
        });
    }

    private void initObservers() {
        recyclerViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.submitList(new ArrayList<>(tasks));
        });

        recyclerViewModel.getCurrentDay().observe(getViewLifecycleOwner(), day -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            dateTextView.setText(sdf.format(day));
        });
    }

    private void initRecycler() {
        taskAdapter = new TaskAdapter(new TaskDiffItemCallback(), task -> {
            Toast.makeText(getContext(), task.getTitle() + "", Toast.LENGTH_SHORT).show();
            recyclerViewModel.getCurrentDay().setValue(new Date(Calendar.getInstance().getTime().getTime() + 3600L *1000*24*Integer.parseInt(task.getTitle())));
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskAdapter);
    }
}
