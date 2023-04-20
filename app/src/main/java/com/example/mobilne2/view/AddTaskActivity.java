package com.example.mobilne2.view;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Database;
import com.example.mobilne2.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddTaskActivity extends AppCompatActivity {

    private TextView dateTextView;
    private Button startTimeBtn;
    private Button endTimeBtn;
    private TextView titleTextView;
    private TextView descTextView;
    private FloatingActionButton saveBtn;
    private FloatingActionButton cancelBtn;
    private ToggleButton lowBtn;
    private ToggleButton midBtn;
    private ToggleButton highBtn;
    private Date date;
    private Date startTime = null;
    private Date endTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);
        init();
    }

    private void init() {
        date = (Date) getIntent().getExtras().get("date");

        initView();
        initListeners();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd. yyyy.", Locale.US);
        dateTextView.setText(sdf.format(date));
    }

    private void initListeners() {
        startTimeBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                    (view, hourOfDay, minute1) -> {
                        c.setTime(date);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute1);
                        startTime = c.getTime();
                    }, hour, minute, true);
            timePickerDialog.setMessage("Pick a start time");
            timePickerDialog.show();
        });

        endTimeBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                    (view, hourOfDay, minute1) -> {
                        c.setTime(date);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute1);
                        endTime = c.getTime();
                    }, hour, minute, true);
            timePickerDialog.setMessage("Pick an end time");
            timePickerDialog.show();
        });

        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        saveBtn.setOnClickListener(v -> {
            if (titleTextView.getText().toString().isEmpty()) {
                Toast.makeText(this, "You must add a title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (descTextView.getText().toString().isEmpty()) {
                Toast.makeText(this, "You must add a description", Toast.LENGTH_SHORT).show();
                return;
            }

            int buttonCount = 0;
            if (lowBtn.isChecked()) buttonCount++;
            if (midBtn.isChecked()) buttonCount++;
            if (highBtn.isChecked()) buttonCount++;
            if (buttonCount != 1) {
                Toast.makeText(this, "You must select one priority", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startTime == null || endTime == null || endTime.before(startTime)) {
                Toast.makeText(this, "You must select a valid time", Toast.LENGTH_SHORT).show();
                return;
            }

            Task.Priority priority = Task.Priority.NONE;
            if (highBtn.isChecked()) priority = Task.Priority.HIGH;
            else if (midBtn.isChecked()) priority = Task.Priority.MID;
            else if (lowBtn.isChecked()) priority = Task.Priority.LOW;

            Task newTask = new Task(
                    Database.getInstance().getUniqueId(),
                    titleTextView.getText().toString(),
                    startTime,
                    endTime,
                    descTextView.getText().toString(),
                    priority
            );

            Set<Task> tasks = Database.getInstance().getAllTasks();
            AtomicBoolean timeslotTaken = new AtomicBoolean(false);
            tasks.forEach(task -> {
                if (task.intersects(newTask)) {
                    timeslotTaken.set(true);
                }
            });

            if (timeslotTaken.get()) {
                Toast.makeText(this, "That timeslot is already taken", Toast.LENGTH_SHORT).show();
                return;
            }

            Database.getInstance().addTask(newTask);

            finish();
        });
    }

    private void initView() {
        dateTextView = findViewById(R.id.addTaskDateTextView);
        lowBtn = findViewById(R.id.addTaskLowBtn);
        midBtn = findViewById(R.id.addTaskMidBtn);
        highBtn = findViewById(R.id.addTaskHighBtn);
        startTimeBtn = findViewById(R.id.startTimeBtn);
        endTimeBtn = findViewById(R.id.endTimeBtn);
        titleTextView = findViewById(R.id.addTitleTextView);
        descTextView = findViewById(R.id.addDescTextView);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
    }
}
