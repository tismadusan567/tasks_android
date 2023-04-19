package com.example.mobilne2.model;

import java.util.SortedSet;
import java.util.TreeSet;

public class Database {

    private SortedSet<Task> allTasks = new TreeSet<>();
    private int uniqueId = 0;

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {

    }

    public int getUniqueId() {
        return uniqueId++;
    }

    public void addTask(Task task) {
        allTasks.add(task);
    }

    public SortedSet<Task> getAllTasks() {
        return allTasks;
    }
}
