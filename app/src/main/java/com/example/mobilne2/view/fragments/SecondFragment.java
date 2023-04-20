package com.example.mobilne2.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.AddTaskActivity;
import com.example.mobilne2.view.TaskDetailActivity;
import com.example.mobilne2.view.recycler.task.TaskAdapter;
import com.example.mobilne2.view.recycler.task.TaskDiffItemCallback;
import com.example.mobilne2.viewmodel.RecyclerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SecondFragment extends Fragment {

    private SearchView searchView;
    private TextView dateTextView;
    private CheckBox checkBox;
    private ToggleButton lowBtn;
    private ToggleButton midBtn;
    private ToggleButton highBtn;
    private RecyclerView recyclerView;
    private FloatingActionButton addObligationBtn;

    private RecyclerViewModel recyclerViewModel;
    private TaskAdapter taskAdapter;

    private Task.Predicate taskTitlePredicate;
    private Task.Predicate priorityPredicate;
    private final Task.Predicate pastObligationsPredicate = t -> t.getEndTime().after(Calendar.getInstance().getTime());
    private final Task.Predicate currentDayPredicate = t -> {
        Calendar c = Calendar.getInstance();
        c.setTime(t.getStartTime());

        Calendar c2 = Calendar.getInstance();
        c2.setTime(recyclerViewModel.getCurrentDay().getValue());

        return c.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                && c.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    };

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
            Intent intent = new Intent(requireActivity(), AddTaskActivity.class);
            intent.putExtra("date", recyclerViewModel.getCurrentDay().getValue());
            startActivity(intent);
        });

        //we want this predicate to be included at the start
        recyclerViewModel.addPredicate(pastObligationsPredicate);
        recyclerViewModel.addPredicate(currentDayPredicate);

        checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                recyclerViewModel.removePredicate(pastObligationsPredicate);
            } else {
                recyclerViewModel.addPredicate(pastObligationsPredicate);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerViewModel.removePredicate(taskTitlePredicate);
                taskTitlePredicate = task -> task.getTitle().toLowerCase(Locale.ROOT).startsWith(s);
                recyclerViewModel.addPredicate(taskTitlePredicate);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerViewModel.removePredicate(taskTitlePredicate);
                taskTitlePredicate = task -> task.getTitle().toLowerCase(Locale.ROOT).startsWith(s);
                recyclerViewModel.addPredicate(taskTitlePredicate);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            recyclerViewModel.removePredicate(taskTitlePredicate);
            return false;
        });

        lowBtn.setOnCheckedChangeListener((btn, isChecked) -> {
            filterByPriority();
        });

        midBtn.setOnCheckedChangeListener((btn, isChecked) -> {
            filterByPriority();
        });

        highBtn.setOnCheckedChangeListener((btn, isChecked) -> {
            filterByPriority();
        });
    }

    private void filterByPriority() {
        recyclerViewModel.removePredicate(priorityPredicate);

        List<Task.Priority> priorities = new ArrayList<>();
        if (lowBtn.isChecked()) priorities.add(Task.Priority.LOW);
        if (midBtn.isChecked()) priorities.add(Task.Priority.MID);
        if (highBtn.isChecked()) priorities.add(Task.Priority.HIGH);

        if (priorities.isEmpty()) {
            priorities.add(Task.Priority.LOW);
            priorities.add(Task.Priority.MID);
            priorities.add(Task.Priority.HIGH);
        }

        priorityPredicate = t -> priorities.stream().anyMatch(priority -> t.getPriority().equals(priority));

        recyclerViewModel.addPredicate(priorityPredicate);
    }

    private void initObservers() {
        recyclerViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.submitList(new ArrayList<>(tasks));
        });

        recyclerViewModel.getCurrentDay().observe(getViewLifecycleOwner(), day -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            dateTextView.setText(sdf.format(day));
            recyclerViewModel.filterTasksByPredicates();
        });
    }

    private void initRecycler() {
        taskAdapter = new TaskAdapter(new TaskDiffItemCallback(), task -> {
            Intent intent = new Intent(requireActivity(), TaskDetailActivity.class);
            intent.putExtra("task", task);
            startActivity(intent);
        }, recyclerViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskAdapter);
    }
}
