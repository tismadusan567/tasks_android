package com.example.mobilne2.view.fragments;

import android.os.Bundle;
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
import com.example.mobilne2.view.recycler.calendar.CalendarDateAdapter;
import com.example.mobilne2.view.recycler.calendar.CalendarDateDiffItemCallback;
import com.example.mobilne2.viewmodel.CalendarViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView calendarDateTextView;
    private CalendarViewModel calendarViewModel;
    private CalendarDateAdapter calendarDateAdapter;

    public FirstFragment() {
        super(R.layout.fragment_first);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarViewModel = new ViewModelProvider(requireActivity()).get(CalendarViewModel.class);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initObservers();
        initRecycler();
    }

    private void initView(View view) {
        calendarDateTextView = view.findViewById(R.id.calendarDateTextView);
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
    }

    private void initObservers() {
        calendarViewModel.getDates().observe(getViewLifecycleOwner(), dates -> {
            calendarDateAdapter.submitList(dates);
        });

        calendarViewModel.getCurrentMonth().observe(getViewLifecycleOwner(), month -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy.", Locale.US);
            calendarDateTextView.setText(sdf.format(month));
        });
    }

    private void initRecycler() {
        calendarDateAdapter = new CalendarDateAdapter(new CalendarDateDiffItemCallback(), date -> {
            Toast.makeText(getContext(), date.getDate() + "", Toast.LENGTH_SHORT).show();
            calendarViewModel.getCurrentMonth().setValue(date.getDate());
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(calendarDateAdapter);
    }
}
