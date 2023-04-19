package com.example.mobilne2.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Comparable<Task>, Serializable {
    private int id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private Priority priority;

    @Override
    public int compareTo(Task o) {
        return this.startTime.compareTo(o.getStartTime());
    }

    public enum Priority {
        NONE,
        LOW,
        MID,
        HIGH
    }

    public Task(int id, String title, Date startTime, Date endTime, String description, Priority priority) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
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

    public interface Predicate {
        boolean satisfiesCondition(Task task);
    }
}
