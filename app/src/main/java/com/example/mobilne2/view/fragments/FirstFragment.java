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
import androidx.viewpager.widget.ViewPager;

import com.example.mobilne2.R;
import com.example.mobilne2.model.CalendarDate;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.BottomNavigationActivity;
import com.example.mobilne2.view.recycler.calendar.CalendarDateAdapter;
import com.example.mobilne2.view.recycler.calendar.CalendarDateDiffItemCallback;
import com.example.mobilne2.view.viewpager.PagerAdapter;
import com.example.mobilne2.viewmodel.CalendarViewModel;
import com.example.mobilne2.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private FloatingActionButton scrollToTodayBtn;

    public FirstFragment() {
        super(R.layout.fragment_first);
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
        calendarDateTextView = view.findViewById(R.id.calendarDateTextView);
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
        scrollToTodayBtn = view.findViewById(R.id.scrollToTodayBtn);
    }

    private void initListeners() {
        scrollToTodayBtn.setOnClickListener(v -> {
            recyclerView.scrollToPosition(recyclerViewModel.getTodayPosition());
        });
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
            recyclerViewModel.getCurrentMonth().setValue(date.getDate());
            recyclerViewModel.getCurrentDay().setValue(date.getDate());

            BottomNavigationView b =  ((BottomNavigationView)requireActivity().findViewById(R.id.bottomNavigation));

            ViewPager viewPager = requireActivity().findViewById(R.id.viewPager);

            b.setOnItemSelectedListener(null);
            b.setSelectedItemId(R.id.navigation_2);
            viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, true);
            b.setOnItemSelectedListener(item -> {
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

        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(calendarDateAdapter);
    }
}
