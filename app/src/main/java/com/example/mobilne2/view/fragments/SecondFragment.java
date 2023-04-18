package com.example.mobilne2.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilne2.R;

public class SecondFragment extends Fragment {

    private SearchView searchView;

    public SecondFragment() {
        super(R.layout.fragment_second);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view) {
        initView(view);
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.searchView);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
}
