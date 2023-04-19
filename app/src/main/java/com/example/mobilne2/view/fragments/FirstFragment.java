package com.example.mobilne2.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilne2.R;
import com.example.mobilne2.model.CalendarDate;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.recycler.calendar.CalendarDateAdapter;
import com.example.mobilne2.view.recycler.calendar.CalendarDateDiffItemCallback;
import com.example.mobilne2.viewmodel.CalendarViewModel;
import com.example.mobilne2.viewmodel.RecyclerViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView calendarDateTextView;
    private RecyclerViewModel recyclerViewModel;
    private CalendarDateAdapter calendarDateAdapter;

    public FirstFragment() {
        super(R.layout.fragment_first);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(recyclerViewModel.getTodayPosition());
    }

    private void init(View view) {
        initView(view);
        initObservers();
        initRecycler();

        for (int i = 0; i <= 1000; i++) {
            Task task = new Task(
                    Integer.toString(i),
                    new Date(Calendar.getInstance().getTime().getTime() + 1000L*3600*i*24),
                    new Date(Calendar.getInstance().getTime().getTime() + 1000L*3600*(i*24 + 1)),
                    "description",
                    i % 2 == 0 ? Task.Priority.HIGH : Task.Priority.LOW
            );
            recyclerViewModel.addTask(task);
        }
    }

    private void initView(View view) {
        calendarDateTextView = view.findViewById(R.id.calendarDateTextView);
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
    }

    private void initObservers() {
        recyclerViewModel.getDates().observe(getViewLifecycleOwner(), dates -> {
            calendarDateAdapter.submitList(new ArrayList<>(dates));
        });

        recyclerViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            calendarDateAdapter.submitList(new ArrayList<>(recyclerViewModel.getDates().getValue()));
        });

        recyclerViewModel.getCurrentMonth().observe(getViewLifecycleOwner(), month -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy.", Locale.US);
            calendarDateTextView.setText(sdf.format(month));
        });


    }

    private void initRecycler() {
        calendarDateAdapter = new CalendarDateAdapter(new CalendarDateDiffItemCallback(), date -> {
            Toast.makeText(getContext(), date.getDate() + "\n" + date.getPriority(), Toast.LENGTH_SHORT).show();
            recyclerViewModel.getCurrentMonth().setValue(date.getDate());
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(calendarDateAdapter);
    }
}
