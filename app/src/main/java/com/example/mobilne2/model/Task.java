package com.example.mobilne2.model;

import java.util.Date;

public class Task {
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private Priority priority;

    public static enum Priority {
        LOW,
        MID,
        HIGH
    }

    public Task(String title, Date startTime, Date endTime, String description, Priority priority) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }
}
