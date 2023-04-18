package com.example.mobilne2.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarDate {
    private Date date;

    private Map<Task.Priority, Integer> priorityCount = new HashMap<>();

    public CalendarDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
