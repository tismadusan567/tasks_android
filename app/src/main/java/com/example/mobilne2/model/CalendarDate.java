package com.example.mobilne2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarDate {
    private Date date;
    private List<Task> tasks = new ArrayList<>();
    private Task.Priority priority = Task.Priority.NONE;
    public CalendarDate(Date date) {
        this.date = date;
    }

    public void addTask(Task task) {
        tasks.add(task);
        updatePriority();
    }

    private void updatePriority() {
        boolean hasHigh = false;
        boolean hasMid = false;
        boolean hasLow = false;

        for(Task task: tasks) {
            switch (task.getPriority()) {
                case HIGH:
                    hasHigh = true;
                    break;
                case MID:
                    hasMid = true;
                    break;
                case LOW:
                    hasLow = true;
                    break;
            }
        }
        if (hasHigh) priority = Task.Priority.HIGH;
        else if (hasMid) priority = Task.Priority.MID;
        else if (hasLow) priority = Task.Priority.LOW;
        else priority = Task.Priority.NONE;
    }

    public Date getDate() {
        return date;
    }

    public Task.Priority getPriority() {
        return priority;
    }
}
