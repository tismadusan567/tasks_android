package com.example.mobilne2.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilne2.R;
import com.example.mobilne2.model.User;
import com.example.mobilne2.viewmodel.RecyclerViewModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class ThirdFragment extends Fragment {

    private TextView nameLabel;
    private TextView emailLabel;
    private Button changePwdBtn;
    private Button logOutBtn;
    private RecyclerViewModel recyclerViewModel;

    public ThirdFragment() {
        super(R.layout.fragment_third);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initObservers();
        initListeners();
    }

    private void initListeners() {
        changePwdBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View view = getLayoutInflater().inflate(R.layout.dialog_change_pwd, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();

            EditText editOldPassword = view.findViewById(R.id.edit_old_password);
            EditText editNewPassword = view.findViewById(R.id.edit_new_password);
            EditText confirmNewPassword = view.findViewById(R.id.confirm_password);
            Button btnCancel = view.findViewById(R.id.button_cancel);
            Button btnConfirm = view.findViewById(R.id.button_confirm);
            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            btnConfirm.setOnClickListener(v12 -> {
                String oldPassword = editOldPassword.getText().toString();
                String newPassword = editNewPassword.getText().toString();
                String confirmPassword = confirmNewPassword.getText().toString();

                if (!oldPassword.equals(recyclerViewModel.getUser().getValue().getPassword())) {
                    Toast.makeText(requireContext(), "Invalid old password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!User.checkPassword(newPassword)) {
                    Toast.makeText(requireContext(), "Invalid new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(requireContext(), "Passwords dont match", Toast.LENGTH_SHORT).show();
                    return;
                }

                recyclerViewModel.getUser().getValue().setPassword(newPassword);

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(requireContext().openFileOutput("password.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(newPassword);
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e);
                }

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("dnevnjak", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(User.PASSWORD, newPassword);

                editor.apply();

                dialog.dismiss();
            });

            dialog.show();

        });

        logOutBtn.setOnClickListener(v -> {
            recyclerViewModel.getUser().getValue().setLoggedIn(false);

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("dnevnjak", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(User.LOGGEDIN, false);

            editor.remove(User.USERNAME);
            editor.remove(User.PASSWORD);
            editor.remove(User.EMAIL);
            editor.remove(User.LOGGEDIN);

            editor.apply();

            getActivity().finish();
        });
    }

    private void initObservers() {
        recyclerViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            nameLabel.setText(user.getUsername());
            emailLabel.setText(user.getEmail());
        });
    }

    private void initView(View view) {
        nameLabel = view.findViewById(R.id.nameLabel);
        emailLabel = view.findViewById(R.id.emailLabel);
        changePwdBtn = view.findViewById(R.id.changePwdBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);
    }
}
