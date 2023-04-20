package com.example.mobilne2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Task implements Comparable<Task>, Serializable {
    private int id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private Priority priority;
    private transient boolean dirty = false;

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

    public boolean intersects(Task other) {
        return other.getStartTime().before(endTime) && other.getEndTime().after(startTime);
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

    public boolean isDirty() {
        return dirty;
    }

    public interface Predicate {
        boolean satisfiesCondition(Task task);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime) && Objects.equals(description, task.description) && priority == task.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, startTime, endTime, description, priority);
    }
}
